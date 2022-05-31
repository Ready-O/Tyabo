package com.tyabo.tyabo.repository

import com.tyabo.tyabo.persistence.SessionDataStore
import com.tyabo.tyabo.service.UserDataSource
import timber.log.Timber
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionDataStore: SessionDataStore
) : SessionRepository
{
    override suspend fun checkUserToken(): Boolean {
        val currentToken = sessionDataStore.getToken()
        return currentToken.isNotEmpty()
    }

    override suspend fun setToken(name: String) {
        sessionDataStore.setToken(name)
    }
}