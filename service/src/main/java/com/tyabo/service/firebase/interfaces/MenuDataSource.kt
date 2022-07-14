package com.tyabo.service.firebase.interfaces

import com.tyabo.data.Menu
import com.tyabo.data.UserType

interface MenuDataSource {

    suspend fun editMenu(menu: Menu, userType: UserType, userId: String): Result<Unit>
    suspend fun fetchMenus(userType: UserType, userId: String, menusIds: List<String>): Result<List<Menu>>
}