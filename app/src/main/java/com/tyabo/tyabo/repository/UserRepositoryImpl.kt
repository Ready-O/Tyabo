package com.tyabo.tyabo.repository

import com.tyabo.tyabo.service.UserDataSource
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository
{
    override fun signIn(): Result<String> {
        return userDataSource.signIn()
    }
}