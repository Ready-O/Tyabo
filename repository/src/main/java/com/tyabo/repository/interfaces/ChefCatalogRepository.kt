package com.tyabo.repository.interfaces

import com.tyabo.common.UiResult
import com.tyabo.data.CatalogItem
import com.tyabo.data.CatalogOrder
import com.tyabo.data.Menu
import kotlinx.coroutines.flow.StateFlow

interface ChefCatalogRepository {

    // Menus
    suspend fun addMenu(menu: Menu, userId: String)
    suspend fun changeHideMenu(menuId: String, isHidden: Boolean, userId: String)
    suspend fun deleteMenu(menuId: String, userId: String)

    // Collections
    suspend fun addCollection(collectionName: String, userId: String)
    suspend fun editCollection(collectionId: String, collectionName: String, userId: String)

    // Catalog
    val catalog: StateFlow<UiResult<List<CatalogItem>>>
    suspend fun updateStateCatalog(userId: String)


}