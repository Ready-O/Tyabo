package com.tyabo.repository.implementation

import com.tyabo.data.Token
import com.tyabo.persistence.interfaces.SessionDataStore
import com.tyabo.repository.interfaces.SessionRepository
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