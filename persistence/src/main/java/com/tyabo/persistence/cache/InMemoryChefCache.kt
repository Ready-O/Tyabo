package com.tyabo.persistence.cache

import com.tyabo.data.CatalogOrder
import com.tyabo.data.Chef
import com.tyabo.data.Collection
import com.tyabo.data.Menu

interface InMemoryChefCache {

    fun updateChef(chef: Chef)
    fun getChef(chefId: String): Result<Chef>
    fun updateMenu(chefId: String, menu: Menu)
    fun deleteMenu(chefId: String, menuId: String)
    fun getMenus(chefId: String): Result<List<Menu>>
    fun updateCollection(chefId: String, collection: Collection)
    fun deleteCollection(chefId: String, collectionId: String)
    fun getCollections(chefId: String): Result<List<Collection>>
    fun updateOrder(chefId: String, order: List<CatalogOrder>)
    fun getOrder(chefId: String): List<CatalogOrder>
}