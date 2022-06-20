package com.tyabo.service.interfaces

import com.tyabo.data.Chef

interface ChefDataSource {

    fun addChef(chef: Chef): Result<Unit>
    suspend fun fetchChef(chefId: String): Result<Chef>
}