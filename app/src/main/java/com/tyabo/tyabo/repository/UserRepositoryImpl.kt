package com.tyabo.tyabo.repository

import com.google.firebase.auth.FirebaseUser
import com.tyabo.tyabo.service.UserDataSource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository
{
    override fun getFirebaseUser(): Result<FirebaseUser> {
        return userDataSource.getFirebaseUser()
    }
}