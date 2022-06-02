package com.tyabo.repository.interfaces

import com.tyabo.data.Token

interface SessionRepository {

    suspend fun checkUserToken(): Result<Token>
    suspend fun setToken(token: Token)
    suspend fun signOut()
}