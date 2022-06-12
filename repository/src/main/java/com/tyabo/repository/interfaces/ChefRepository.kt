package com.tyabo.repository.interfaces

import com.tyabo.data.Chef

interface ChefRepository {

    fun addChef(chef: Chef)
    suspend fun getChef(chefId: String): Result<Chef>
}