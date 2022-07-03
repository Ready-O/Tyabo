package com.tyabo.repository.implementation

import com.tyabo.common.UiResult
import com.tyabo.data.*
import com.tyabo.data.Collection
import com.tyabo.persistence.cache.InMemoryChefCache
import com.tyabo.repository.interfaces.ChefRepository
import com.tyabo.service.interfaces.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class ChefRepositoryImpl @Inject constructor(
    private val chefCache: InMemoryChefCache,
    private val chefDataSource: ChefDataSource,
    private val catalogOrderDataSource: CatalogOrderDataSource,
    private val menuDataSource: MenuDataSource,
    private val menuUploadSource: MenuUploadSource,
    private val collectionDataSource: CollectionDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : ChefRepository {

    override suspend fun addChef(userId: String, name: String) {
        withContext(ioDispatcher){
            val generatedId = UUID.randomUUID().toString()
            val chef = Chef(id = userId, name = name, catalogOrderId = generatedId)
            chefDataSource.updateChef(chef).onSuccess {
                catalogOrderDataSource.updateCatalogOrder(
                    catalogOrderId = chef.catalogOrderId,
                    catalogOrder = listOf(),
                    userType = UserType.Chef,
                    userId = chef.id
                ).onSuccess {
                    chefCache.updateChef(chef)
                }
            }
        }
    }

    override fun getChef(chefId: String): Flow<Result<Chef>> = flow {
        chefCache.getChef(chefId)
            .onSuccess {
                emit(Result.success(it))
            }
            .onFailure {
                chefDataSource.fetchChef(chefId)
                    .onSuccess { chef ->
                        chefCache.updateChef(chef)
                        emit(Result.success(chef))
                    }
                    .onFailure {
                        emit(Result.failure<Chef>(Exception()))
                    }
            }
    }.flowOn(ioDispatcher)

    override suspend fun addMenu(menu: Menu, userId: String) {
        withContext(ioDispatcher){
            uploadMenuPicture(chefId = userId, menuId = menu.id, menuPictureUrl = menu.menuPictureUrl)
                .onSuccess { downloadUrl ->
                    val newMenu = menu.copy(menuPictureUrl = downloadUrl)
                    menuDataSource.addMenu(
                        menu = newMenu,
                        userType = UserType.Chef,
                        userId = userId
                    )
                        .onSuccess {
                            chefCache.updateMenu(chefId = userId, menu = newMenu)
                            chefCache.getChef(userId).onSuccess { chef ->
                                val updatedOrder = chefCache.getOrder(chef.id).toMutableList()
                                updatedOrder.add(
                                    index = 0,
                                    element = CatalogOrder(id = menu.id, catalogItemType = CatalogItemType.MENU)
                                )
                                updateChefCatalogOrder(
                                    catalogOrderId = chef.catalogOrderId,
                                    catalogOrder = updatedOrder,
                                    chefId = chef.id
                                )
                            }
                        }
                        .onFailure {
                            menuUploadSource.deleteMenuPicture(
                                userId = userId,
                                menuId = menu.id,
                                userType = UserType.Chef
                            )
                        }
                }
        }
    }

    private suspend fun uploadMenuPicture(
        chefId: String,
        menuId: String,
        menuPictureUrl: String?
    ): Result<String?> {
        return if (menuPictureUrl == null){
            Result.success(null)
        } else {
            menuUploadSource.uploadMenuPicture(
                userId = chefId,
                menuId = menuId,
                pictureUrl = menuPictureUrl,
                userType = UserType.Chef
            )
        }
    }

    override fun getMenus(chefId: String): Flow<UiResult<List<Menu>>> = flow {
        emit(UiResult.Loading)
        chefCache.getMenus(chefId)
            .onSuccess { emit(UiResult.Success(it)) }
            .onFailure {
                val menusToFetch = mutableListOf<String>()
                chefCache.getOrder(chefId).forEach {
                    if(it.catalogItemType == CatalogItemType.MENU){
                        menusToFetch.add(it.id)
                    }
                }
                menuDataSource.fetchMenus(userType = UserType.Chef, userId = chefId, menusIds = menusToFetch)
                    .onSuccess {  menus ->
                        menus.forEach {
                            chefCache.updateMenu(chefId = chefId, menu = it)
                        }
                        emit(UiResult.Success(menus))
                    }
                    .onFailure {
                        emit(UiResult.Failure(Exception()))
                    }
            }
    }.flowOn(ioDispatcher)

    override suspend fun addCollection(collectionName: String, userId: String) {
        withContext(ioDispatcher){
            val generatedId = UUID.randomUUID().toString()
            val collection = Collection(id = generatedId, name = collectionName)
            collectionDataSource.addCollection(
                collection = collection,
                userType = UserType.Chef,
                userId = userId
            ).onSuccess {
                chefCache.updateCollection(chefId = userId, collection = collection)
                chefCache.getChef(userId).onSuccess { chef ->
                    val updatedOrder = chefCache.getOrder(chef.id).toMutableList()
                    updatedOrder.add(
                        index = 0,
                        element = CatalogOrder(id = collection.id, catalogItemType = CatalogItemType.COLLECTION)
                    )
                    updateChefCatalogOrder(
                        catalogOrderId = chef.catalogOrderId,
                        catalogOrder = updatedOrder,
                        chefId = chef.id
                    )
                }
            }
        }
    }

    override suspend fun updateCatalogOrder(
        chefId: String,
        catalogOrder: List<CatalogOrder>
    ) {
        withContext(ioDispatcher){
            chefCache.getChef(chefId).onSuccess {
                updateChefCatalogOrder(
                    catalogOrderId = it.catalogOrderId,
                    catalogOrder = catalogOrder,
                    chefId = it.id
                )
            }
        }
    }

    private suspend fun updateChefCatalogOrder(
        catalogOrderId: String,
        catalogOrder: List<CatalogOrder>,
        chefId: String
    ){
        catalogOrderDataSource.updateCatalogOrder(
            catalogOrderId = catalogOrderId,
            catalogOrder = catalogOrder,
            userType = UserType.Chef,
            userId = chefId
        ).onSuccess {
            chefCache.updateOrder(chefId = chefId, order = catalogOrder)
        }
    }

    // Cache will contain only the catalog elements to display
    override fun getCatalogOrder(chefId: String, count: Long): Flow<List<CatalogOrder>> = flow<List<CatalogOrder>> {
        val list = chefCache.getOrder(chefId)
        if (list.isNotEmpty()) {
            emit(list)
        }
        else{
            chefCache.getChef(chefId).onSuccess { chef ->
                catalogOrderDataSource.fetchCatalogOrder(
                    catalogOrderId = chef.catalogOrderId,
                    userType = UserType.Chef,
                    userId = chef.id
                ).onSuccess { list ->
                    chefCache.updateOrder(chefId = chef.id, order = list.take(count.toInt()))
                    emit(list.take(count.toInt()))
                }.onFailure {
                    emit(listOf())
                }
            }
        }
    }.flowOn(ioDispatcher)
}