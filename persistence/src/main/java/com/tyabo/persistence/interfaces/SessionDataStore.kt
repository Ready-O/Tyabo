package com.tyabo.persistence.interfaces

import com.tyabo.data.Token

interface SessionDataStore {

    suspend fun getToken(): Result<Token>
    suspend fun setToken(token: Token)
    suspend fun clearToken()

}