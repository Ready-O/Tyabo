package com.tyabo.repository.interfaces

import com.tyabo.common.UiResult
import com.tyabo.data.Chef
import com.tyabo.data.Menu
import kotlinx.coroutines.flow.Flow

interface ChefRepository {

    suspend fun addChef(chef: Chef)
    fun getChef(chefId: String): Flow<Result<Chef>>
    suspend fun addMenu(menu: Menu, userId: String)
    fun getMenus(chefId: String): Flow<UiResult<List<Menu>>>
}