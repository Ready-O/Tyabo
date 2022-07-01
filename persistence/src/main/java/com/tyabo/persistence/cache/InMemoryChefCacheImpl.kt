package com.tyabo.persistence.cache

import com.tyabo.data.Chef
import com.tyabo.data.Menu
import javax.inject.Inject

class InMemoryChefCacheImpl @Inject constructor(
    private val chefMap: LinkedHashMap<String,Chef>,
    private val menuMap: LinkedHashMap<String,LinkedHashMap<String, Menu>>
) : InMemoryChefCache {

    override fun updateChef(chef: Chef) {
        chefMap[chef.id] = chef
        if (menuMap[chef.id] == null) menuMap[chef.id] = LinkedHashMap<String, Menu>()
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
}