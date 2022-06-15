package com.tyabo.repository.interfaces

import com.tyabo.common.FlowResult
import com.tyabo.data.FirebaseUser
import com.tyabo.data.UserType
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getFirebaseUser(): Result<FirebaseUser>
    suspend fun checkUserType(userId: String): Flow<FlowResult<UserType>>
    suspend fun addUser(userId: String, name: String, userType: UserType)
}