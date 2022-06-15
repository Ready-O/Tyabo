package com.tyabo.service.interfaces

import com.tyabo.data.Chef

interface ChefDataSource {

    fun addChef(chef: Chef)
    suspend fun fetchChef(chefId: String): Result<Chef>
}