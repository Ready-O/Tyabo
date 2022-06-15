package com.tyabo.repository.implementation

import com.tyabo.data.Token
import com.tyabo.persistence.datastore.SessionDataStore
import com.tyabo.repository.interfaces.SessionRepository
import com.tyabo.service.interfaces.FirebaseAuthDataSource
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionDataStore: SessionDataStore,
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
) : SessionRepository
{
    override suspend fun checkUserToken(): Result<Token> {
        return sessionDataStore.getToken()
    }

    override suspend fun setToken(token: Token) {
        sessionDataStore.setToken(token)
    }

    override suspend fun signOut() {
        sessionDataStore.clearToken()
        firebaseAuthDataSource.signOut()
    }
}