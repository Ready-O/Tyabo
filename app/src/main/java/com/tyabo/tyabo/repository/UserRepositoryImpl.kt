package com.tyabo.tyabo.repository

import com.tyabo.tyabo.service.UserDataSource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository
{
    override fun signIn() {
        userDataSource.signIn()
    }
}