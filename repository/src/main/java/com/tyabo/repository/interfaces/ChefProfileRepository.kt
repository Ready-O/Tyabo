package com.tyabo.repository.interfaces

import com.tyabo.common.UiResult
import com.tyabo.data.Chef
import kotlinx.coroutines.flow.StateFlow

interface ChefProfileRepository {

    val chefFlow: StateFlow<UiResult<Chef>>
    suspend fun updateStateChef(userId: String)
    suspend fun editChef(
        userId: String,
        name: String,
        phoneNumber: String,
        chefPictureUrl: String?,
        businessPictureUrl: String?,
        bio: String
    )
}