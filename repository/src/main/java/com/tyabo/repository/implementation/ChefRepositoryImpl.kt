package com.tyabo.repository.implementation

import com.tyabo.common.UiResult
import com.tyabo.data.CatalogOrder
import com.tyabo.data.Chef
import com.tyabo.data.Menu
import com.tyabo.data.UserType
import com.tyabo.persistence.cache.InMemoryChefCache
import com.tyabo.repository.interfaces.ChefRepository
import com.tyabo.service.interfaces.ChefDataSource
import com.tyabo.service.interfaces.MenuDataSource
import com.tyabo.service.interfaces.MenuUploadSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChefRepositoryImpl @Inject constructor(
    private val chefCache: InMemoryChefCache,
    private val chefDataSource: ChefDataSource,
    private val menuDataSource: MenuDataSource,
    private val menuUploadSource: MenuUploadSource,
    private val ioDispatcher: CoroutineDispatcher
) : ChefRepository {

    override suspend fun addChef(chef: Chef) {
        withContext(ioDispatcher){
            chefDataSource.updateChef(chef).onSuccess {
                chefCache.updateChef(chef)
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
                                val updatedOrder = chef.catalogOrder.toMutableList()
                                updatedOrder.add(index = 0, element = CatalogOrder(id = menu.id))
                                updateChefCatalogOrder(chef = chef, updatedOrder)
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
                chefCache.getChef(chefId).onSuccess { chef ->
                    val firstMenus = chef.catalogOrder.take(3)
                    menuDataSource.fetchMenus(userType = UserType.Chef, userId = chefId, catalogToFetch = firstMenus)
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
            }
    }.flowOn(ioDispatcher)

    override suspend fun updateCatalogOrder(
        chefId: String,
        catalogOrder: MutableList<CatalogOrder>
    ) {
        withContext(ioDispatcher){
            chefCache.getChef(chefId).onSuccess {
                updateChefCatalogOrder(it, catalogOrder)
            }
        }
    }

    private suspend fun updateChefCatalogOrder(
        chef: Chef,
        catalogOrder: MutableList<CatalogOrder>
    ){
        val updatedChef = chef.copy(catalogOrder = catalogOrder)
        chefDataSource.updateChef(updatedChef).onSuccess {
            chefCache.updateChef(updatedChef)
        }
    }
}