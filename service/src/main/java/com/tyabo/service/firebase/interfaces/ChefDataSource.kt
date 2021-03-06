package com.tyabo.service.firebase.interfaces

import com.tyabo.data.Chef

interface ChefDataSource {

    fun updateChef(chef: Chef): Result<Unit>
    suspend fun fetchChef(chefId: String): Result<Chef>
}