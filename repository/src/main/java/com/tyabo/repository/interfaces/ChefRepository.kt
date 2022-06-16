package com.tyabo.repository.interfaces

import com.tyabo.common.UiResult
import com.tyabo.data.Chef
import kotlinx.coroutines.flow.Flow

interface ChefRepository {

    suspend fun addChef(chef: Chef)
    fun getChef(chefId: String): Flow<Result<Chef>>
}