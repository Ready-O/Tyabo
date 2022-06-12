package com.tyabo.repository.interfaces

import com.tyabo.data.FirebaseUser
import com.tyabo.data.UserType

interface UserRepository {

    fun getFirebaseUser(): Result<FirebaseUser>
    suspend fun checkUserType(userId: String): Result<UserType>
    suspend fun addUser(userId: String, name: String, userType: UserType)
}