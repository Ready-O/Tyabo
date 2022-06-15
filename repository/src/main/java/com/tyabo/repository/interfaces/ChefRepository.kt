package com.tyabo.repository.interfaces

import com.tyabo.common.FlowResult
import com.tyabo.data.Chef
import kotlinx.coroutines.flow.Flow

interface ChefRepository {

    suspend fun addChef(chef: Chef)
    suspend fun getChef(chefId: String): Flow<FlowResult<Chef>>
}