package com.tyabo.repository.interfaces

import com.tyabo.common.UiResult
import com.tyabo.data.*
import com.tyabo.data.Collection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ChefRepository {

    suspend fun addChef(userId: String, name: String)
    fun getChef(chefId: String): Flow<Result<Chef>>
}

