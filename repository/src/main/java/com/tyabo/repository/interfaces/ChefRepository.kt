package com.tyabo.repository.interfaces

import com.tyabo.common.UiResult
import com.tyabo.data.CatalogOrder
import com.tyabo.data.Chef
import com.tyabo.data.Collection
import com.tyabo.data.Menu
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ChefRepository {

    suspend fun addChef(userId: String, name: String)
    fun getChef(chefId: String): Flow<Result<Chef>>
    suspend fun addMenu(menu: Menu, userId: String)
    fun getMenus(chefId: String, menusIds: List<String>): Flow<UiResult<List<Menu>>>
    suspend fun addCollection(collectionName: String, userId: String)
    fun getCollections(chefId: String, collectionsIds: List<String>): Flow<UiResult<List<Collection>>>

    val catalogOrder: StateFlow<List<CatalogOrder>>
    suspend fun updateCatalogOrder(chefId: String, catalogOrder: List<CatalogOrder>)
    suspend fun updateStateCatalogOrder(chefId: String, count: Long = DEFAULT_COUNT)

    companion object {
        private const val DEFAULT_COUNT: Long = 10
    }
}

