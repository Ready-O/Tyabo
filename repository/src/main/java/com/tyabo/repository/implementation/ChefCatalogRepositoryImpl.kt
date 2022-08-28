package com.tyabo.repository.implementation

import com.tyabo.common.UiResult
import com.tyabo.data.*
import com.tyabo.persistence.cache.InMemoryChefCache
import com.tyabo.repository.interfaces.ChefCatalogRepository
import com.tyabo.repository.interfaces.ChefCollectionRepository
import com.tyabo.repository.interfaces.ChefMenuRepository
import com.tyabo.service.firebase.interfaces.CatalogOrderDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChefCatalogRepositoryImpl @Inject constructor(
    private val chefCache: InMemoryChefCache,
    private val menuRepository: ChefMenuRepository,
    private val collectionRepository: ChefCollectionRepository,
    private val catalogOrderDataSource: CatalogOrderDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : ChefCatalogRepository {

    // Menus
    override suspend fun addMenu(menu: Menu, userId: String) {
        withContext(ioDispatcher){
            menuRepository.addMenu(menu = menu, userId = userId)
                .onSuccess{
                    addNewItemOrder(
                        userId = userId,
                        itemId = menu.id,
                        itemType = CatalogItemType.MENU
                    )
                }
        }
    }

    override suspend fun changeHideMenu(menuId: String, isHidden: Boolean, userId: String) {
        withContext(ioDispatcher){
            menuRepository.changeHideMenu(menuId,isHidden,userId)
            updateState(userId = userId, order = chefCache.getOrder(userId))
        }
    }

    override suspend fun deleteMenu(menuId: String, userId: String) {
        withContext(ioDispatcher){
            menuRepository.deleteMenu(menuId = menuId, userId = userId)
                .onSuccess {
                    deleteItemOrder(
                        userId = userId,
                        itemId = menuId
                    )
                }
        }
    }

    // Collections
    override suspend fun addCollection(collectionName: String, userId: String) {
        withContext(ioDispatcher){
            collectionRepository.addCollection(collectionName = collectionName, userId = userId)
                .onSuccess { collectionId ->
                    addNewItemOrder(
                        userId = userId,
                        itemId = collectionId,
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
            collectionRepository.editCollection(
                collectionId = collectionId,
                collectionName = collectionName,
                userId = userId
            )
            updateState(userId = userId, order = chefCache.getOrder(userId))
        }
    }

    // Catalog

    private val _catalog = MutableStateFlow<UiResult<List<CatalogItem>>>(UiResult.Loading)
    override val catalog: StateFlow<UiResult<List<CatalogItem>>> = _catalog.asStateFlow()

    override suspend fun updateStateCatalog(userId: String) {
        withContext(ioDispatcher){
            val order = chefCache.getOrder(userId)
            if (order.isNotEmpty()) {
                updateState(userId = userId, order = order)
            }
            else{
                updateRemoteCatalogOrder(userId)
            }
        }
    }

    private suspend fun updateState(userId: String, order: List<CatalogOrder>){
        val menusIds = mutableListOf<String>()
        val collectionsIds = mutableListOf<String>()
        order.forEach {
            if (it.catalogItemType == CatalogItemType.MENU){
                menusIds.add(it.id)
            }
            else {
                collectionsIds.add(it.id)
            }
        }
        combine(
            menuRepository.getMenus(userId = userId, menusIds = menusIds),
            collectionRepository.getCollections(userId = userId, collectionsIds = collectionsIds)
        ) { menusResult,collectionsResult ->
            if (menusResult.isSuccess && collectionsResult.isSuccess) {
                val tempCatalog = mutableListOf<CatalogItem>()
                menusResult.getOrThrow().forEach {
                    tempCatalog.add(it.toMenuItem())
                }
                collectionsResult.getOrThrow().forEach {
                    tempCatalog.add(it.toCollectionItem())
                }
                val sortedCatalog = mutableListOf<CatalogItem>()
                order.forEach { catalogOrder ->
                    val item = tempCatalog.find { it.id == catalogOrder.id }
                    if (item != null) {
                        sortedCatalog.add(item)
                    }
                }
                _catalog.value = UiResult.Success(sortedCatalog)
            } else if (menusResult.isFailure || collectionsResult.isFailure) {
                _catalog.value = UiResult.Failure()
            } else {
                _catalog.value = UiResult.Loading
            }
        }.collectLatest {  }
    }

    private suspend fun updateRemoteCatalogOrder(userId: String) {
        chefCache.getChef(userId).onSuccess { chef ->
            catalogOrderDataSource.fetchCatalogOrder(
                catalogOrderId = chef.catalogOrderId,
                userType = UserType.Chef,
                userId = chef.id
            ).onSuccess { list ->
                chefCache.updateOrder(chefId = userId, order = list)
                updateState(userId = userId, order = list)
            }.onFailure {
                updateState(userId = userId, order = listOf())
            }
        }
    }

    override suspend fun editCatalogOrder(userId: String, newCatalog: List<CatalogItem>) {
        val newOrder = mutableListOf<CatalogOrder>()
        newCatalog.forEach {
            newOrder.add(it.toCatalogOrder())
        }
        chefCache.getChef(userId).onSuccess { chef ->
            updateCatalogOrder(
                catalogOrderId = chef.catalogOrderId,
                catalogOrder = newOrder,
                userId = chef.id
            )
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
            updateCatalogOrder(
                catalogOrderId = chef.catalogOrderId,
                catalogOrder = updatedOrder,
                userId = chef.id
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
            updateCatalogOrder(
                catalogOrderId = chef.catalogOrderId,
                catalogOrder = updatedOrder,
                userId = chef.id
            )
        }
    }

    private suspend fun updateCatalogOrder(
        catalogOrderId: String,
        catalogOrder: List<CatalogOrder>,
        userId: String
    ){
        catalogOrderDataSource.updateCatalogOrder(
            catalogOrderId = catalogOrderId,
            catalogOrder = catalogOrder,
            userType = UserType.Chef,
            userId = userId
        ).onSuccess {
            chefCache.updateOrder(chefId = userId, order = catalogOrder)
            updateState(userId = userId, order = chefCache.getOrder(userId))
        }
    }
}