package com.tyabo.tyabo.repository

import com.tyabo.tyabo.data.Token

interface SessionRepository {

    suspend fun checkUserToken(): Result<Token>
    suspend fun setToken(token: Token)
}