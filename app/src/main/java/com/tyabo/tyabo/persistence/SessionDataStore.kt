package com.tyabo.tyabo.persistence

interface SessionDataStore {

    suspend fun getToken(): String
    suspend fun setToken(name: String)

}