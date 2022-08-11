package com.tyabo.repository.implementation

import com.tyabo.common.UiResult
import com.tyabo.data.*
import com.tyabo.data.Collection
import com.tyabo.persistence.cache.InMemoryChefCache
import com.tyabo.repository.interfaces.ChefRepository
import com.tyabo.service.firebase.interfaces.*
import com.tyabo.service.retrofit.MenuVideoDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
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
    private val menuVideoDataSource: MenuVideoDataSource,
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

    override suspend fun editMenu(menu: Menu, userId: String) {
        withContext(ioDispatcher){
            val oldMenu = chefCache.getMenus(userId).getOrThrow().find { it.id == menu.id }
            when {
                // New menu
                oldMenu == null -> {
                    uploadMenuPicture(chefId = userId, menuId = menu.id, menuPictureUrl = menu.menuPictureUrl)
                        .onSuccess { downloadUrl ->
                            val newMenu = menu.copy(menuPictureUrl = downloadUrl)
                            updateMenuFirestore(menu = newMenu, chefId = userId).onSuccess {
                                addNewItemOrder(
                                    userId = userId,
                                    itemId = menu.id,
                                    itemType = CatalogItemType.MENU
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
                // Picture modified
                menu.menuPictureUrl != oldMenu.menuPictureUrl -> {
                    menuUploadSource.deleteMenuPicture(
                        userId = userId,
                        menuId = menu.id,
                        userType = UserType.Chef
                    )
                    uploadMenuPicture(chefId = userId, menuId = menu.id, menuPictureUrl = menu.menuPictureUrl)
                        .onSuccess { downloadUrl ->
                            val newMenu = menu.copy(menuPictureUrl = downloadUrl)
                            updateMenuFirestore(menu = newMenu, chefId = userId)
                        }
                        .onFailure {
                            menuUploadSource.deleteMenuPicture(
                                userId = userId,
                                menuId = menu.id,
                                userType = UserType.Chef
                            )
                        }
                }
                else -> {
                    updateMenuFirestore(menu = menu, chefId = userId)
                }
            }
        }
    }

    private suspend fun updateMenuFirestore(
        menu: Menu,
        chefId: String,
    ): Result<Unit> {
        return menuDataSource.editMenu(
            menu = menu,
            userType = UserType.Chef,
            userId = chefId
        )
            .onSuccess {
                chefCache.updateMenu(chefId = chefId, menu = menu)
                _catalogOrder.value = chefCache.getOrder(chefId)
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

    override suspend fun changeHideMenu(menuId: String, isHidden: Boolean, userId: String) {
        withContext(ioDispatcher) {
            val menu = chefCache.getMenus(userId).getOrThrow().find { it.id == menuId }
            updateMenuFirestore(
                menu = menu!!.copy(isHidden = isHidden),
                chefId = userId
            )
        }
    }

    override suspend fun deleteMenu(menuId: String, userId: String) {
        withContext(ioDispatcher) {
            menuDataSource.deleteMenu(
                menuId = menuId,
                userId = userId,
                userType = UserType.Chef
            ).onSuccess {
                chefCache.deleteMenu(
                    chefId = userId,
                    menuId = menuId
                )
                deleteItemOrder(
                    userId = userId,
                    itemId = menuId
                )
            }
        }
    }

    override suspend fun getVideo(url: String): Result<MenuYoutubeVideo> {
        return menuVideoDataSource.importVideo(url)
    }

    override fun getMenus(chefId: String, menusIds: List<String>): Flow<UiResult<List<Menu>>> = flow {
        emit(UiResult.Loading)
        chefCache.getMenus(chefId)
            .onSuccess { emit(UiResult.Success(it)) }
            .onFailure {
                menuDataSource.fetchMenus(userType = UserType.Chef, userId = chefId, menusIds = menusIds)
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
            collectionDataSource.editCollection(
                collection = collection,
                userType = UserType.Chef,
                userId = userId
            ).onSuccess {
                chefCache.updateCollection(chefId = userId, collection = collection)
                addNewItemOrder(
                    userId = userId,
                    itemId = collection.id,
                    itemType = CatalogItemType.COLLECTION
                )
            }
        }
    }

    override suspend fun editCollection(
        collectionId: String,
        collectionName: String,
        userId: String
    ) {
        withContext(ioDispatcher){
            val collection = Collection(id = collectionId, name = collectionName)
            collectionDataSource.editCollection(
                collection = collection,
                userType = UserType.Chef,
                userId = userId
            )
                .onSuccess {
                    chefCache.updateCollection(chefId = userId, collection = collection)
                    _catalogOrder.value = chefCache.getOrder(userId)
                }
        }
    }

    override fun getCollections(
        chefId: String,
        collectionsIds: List<String>
    ): Flow<UiResult<List<Collection>>> = flow {
        emit(UiResult.Loading)
        chefCache.getCollections(chefId)
            .onSuccess { emit(UiResult.Success(it)) }
            .onFailure {
                collectionDataSource.fetchCollections(userType = UserType.Chef, userId = chefId, collectionsIds = collectionsIds)
                    .onSuccess {  collections ->
                        collections.forEach {
                            chefCache.updateCollection(chefId = chefId, collection = it)
                        }
                        emit(UiResult.Success(collections))
                    }
                    .onFailure {
                        emit(UiResult.Failure(Exception()))
                    }
            }
    }.flowOn(ioDispatcher)

    private val _catalogOrder = MutableStateFlow<List<CatalogOrder>>(listOf())
    override val catalogOrder: StateFlow<List<CatalogOrder>> = _catalogOrder.asStateFlow()

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
            _catalogOrder.value = chefCache.getOrder(chefId)
        }
    }

    private suspend fun addNewItemOrder(
        userId: String,
        itemId: String,
        itemType: CatalogItemType
    ) {
        chefCache.getChef(userId).onSuccess { chef ->
            val updatedOrder = chefCache.getOrder(chef.id).toMutableList()
            updatedOrder.add(
                index = 0,
                element = CatalogOrder(
                    id = itemId,
                    catalogItemType = itemType
                )
            )
            updateChefCatalogOrder(
                catalogOrderId = chef.catalogOrderId,
                catalogOrder = updatedOrder,
                chefId = chef.id
            )
        }
    }

    private suspend fun deleteItemOrder(
        userId: String,
        itemId: String,
    ){
        chefCache.getChef(userId).onSuccess { chef ->
            val updatedOrder = chefCache.getOrder(chef.id).toMutableList()
            updatedOrder.removeIf { it.id == itemId }
            updateChefCatalogOrder(
                catalogOrderId = chef.catalogOrderId,
                catalogOrder = updatedOrder,
                chefId = chef.id
            )
        }
    }

    // Cache will contain only the catalog elements to display
    override suspend fun updateStateCatalogOrder(chefId: String, count: Long) {
        withContext(ioDispatcher){
            val list = chefCache.getOrder(chefId)
            if (list.isNotEmpty()) {
                _catalogOrder.value = list
            }
            else{
                updateRemoteCatalogOrder(chefId, count)
            }
        }
    }

    private suspend fun updateRemoteCatalogOrder(chefId: String, count: Long) {
        chefCache.getChef(chefId).onSuccess { chef ->
            catalogOrderDataSource.fetchCatalogOrder(
                catalogOrderId = chef.catalogOrderId,
                userType = UserType.Chef,
                userId = chef.id
            ).onSuccess { list ->
                chefCache.updateOrder(chefId = chef.id, order = list.take(count.toInt()))
                _catalogOrder.value = list.take(count.toInt())
            }.onFailure {
                _catalogOrder.value = listOf()
            }
        }
    }
}