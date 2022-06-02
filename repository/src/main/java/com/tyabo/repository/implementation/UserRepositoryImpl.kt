package com.tyabo.repository.implementation

import com.tyabo.data.FirebaseUser
import com.tyabo.repository.interfaces.UserRepository
import com.tyabo.service.interfaces.FirebaseAuthDataSource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: FirebaseAuthDataSource
) : UserRepository
{
    override fun getFirebaseUser(): Result<FirebaseUser> {
        return userDataSource.getFirebaseUser()
    }
}