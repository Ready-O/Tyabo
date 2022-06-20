package com.tyabo.repository.interfaces

import com.tyabo.common.UiResult
import com.tyabo.data.FirebaseUser
import com.tyabo.data.UserType
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserId(): Result<String>
    fun getUserType(): Result<UserType>
    fun getFirebaseUser(): Result<FirebaseUser>
    suspend fun checkUserType(userId: String): Flow<Result<UserType>>
    suspend fun addUser(userId: String, name: String, userType: UserType)
}