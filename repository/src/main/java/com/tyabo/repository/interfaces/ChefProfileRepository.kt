package com.tyabo.repository.interfaces

import com.tyabo.common.UiResult
import com.tyabo.data.Chef
import kotlinx.coroutines.flow.StateFlow

interface ChefProfileRepository {

    val chefFlow: StateFlow<UiResult<Chef>>
    suspend fun updateStateChef(userId: String)
}