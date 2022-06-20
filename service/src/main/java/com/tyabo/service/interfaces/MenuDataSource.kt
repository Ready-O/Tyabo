package com.tyabo.service.interfaces

import com.tyabo.data.Menu
import com.tyabo.data.UserType

interface MenuDataSource {

    suspend fun addMenu(menu: Menu, userType: UserType, userId: String): Result<Unit>
}