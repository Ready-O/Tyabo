package com.tyabo.tyabo.repository

interface SessionRepository {

    suspend fun checkUserToken(): Boolean
    suspend fun setToken(name: String)
}