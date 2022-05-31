package com.tyabo.tyabo.repository

import com.tyabo.data.Token
import com.tyabo.tyabo.persistence.SessionDataStore
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionDataStore: SessionDataStore
) : SessionRepository
{
    override suspend fun checkUserToken(): Result<Token> {
        return sessionDataStore.getToken()
    }

    override suspend fun setToken(token: Token) {
        sessionDataStore.setToken(token)
    }
}