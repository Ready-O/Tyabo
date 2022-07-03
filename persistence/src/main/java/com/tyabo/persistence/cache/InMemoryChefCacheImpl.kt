package com.tyabo.persistence.cache

import com.tyabo.data.CatalogOrder
import com.tyabo.data.Chef
import com.tyabo.data.Collection
import com.tyabo.data.Menu
import javax.inject.Inject

class InMemoryChefCacheImpl @Inject constructor(
    private val chefMap: LinkedHashMap<String,Chef>,
    private val menuMap: LinkedHashMap<String,LinkedHashMap<String, Menu>>,
    private val collectionMap: LinkedHashMap<String,LinkedHashMap<String, Collection>>,
    private val catalogOrder: LinkedHashMap<String, List<CatalogOrder>>
) : InMemoryChefCache {

    override fun updateChef(chef: Chef) {
        chefMap[chef.id] = chef
        if (menuMap[chef.id] == null) {
            menuMap[chef.id] = LinkedHashMap<String, Menu>()
            collectionMap[chef.id] = LinkedHashMap<String, Collection>()
            catalogOrder[chef.id] = listOf()
        }
    }

    override fun getChef(chefId: String): Result<Chef> {
        val item = chefMap[chefId]
        return if (item != null) Result.success(item) else Result.failure(NoSuchElementException())
    }

    override fun updateMenu(chefId: String, menu: Menu) {
        val chefMap = menuMap[chefId]!!
        chefMap[menu.id] = menu
    }

    override fun deleteMenu(chefId: String, menuId: String) {
        val chefMap = menuMap[chefId]
        chefMap?.remove(menuId)
    }

    override fun getMenus(chefId: String): Result<List<Menu>> {
        val menuMap = menuMap[chefId]!!
        val menus = menuMap.values.toList()
        return if (menus.isEmpty()) Result.failure(NoSuchElementException()) else Result.success(menus)
    }

    override fun updateCollection(chefId: String, collection: Collection) {
        val chefMap = collectionMap[chefId]!!
        chefMap[collection.id] = collection
    }

    override fun getCollections(chefId: String): Result<List<Collection>> {
        val collectionMap = collectionMap[chefId]!!
        val collections = collectionMap.values.toList()
        return if (collections.isEmpty()) Result.failure(NoSuchElementException()) else Result.success(collections)
    }
}