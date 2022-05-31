package com.tyabo.tyabo.persistence

import com.tyabo.tyabo.data.Token

interface SessionDataStore {

    suspend fun getToken(): Result<Token>
    suspend fun setToken(token: Token)

}