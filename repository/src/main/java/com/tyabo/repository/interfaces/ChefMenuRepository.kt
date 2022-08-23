package com.tyabo.repository.interfaces

import com.tyabo.common.UiResult
import com.tyabo.data.Menu
import com.tyabo.data.MenuYoutubeVideo
import kotlinx.coroutines.flow.Flow

interface ChefMenuRepository {

    suspend fun addMenu(menu: Menu, userId: String): Result<Unit>
    suspend fun editMenu(menu: Menu, userId: String)
    suspend fun changeHideMenu(menuId: String, isHidden: Boolean, userId: String)
    suspend fun deleteMenu(menuId: String, userId: String): Result<Unit>
    fun getMenus(userId: String, menusIds: List<String>): Flow<Result<List<Menu>>>
    suspend fun getVideo(url: String): Result<MenuYoutubeVideo>
}