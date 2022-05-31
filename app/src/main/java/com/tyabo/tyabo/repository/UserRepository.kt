package com.tyabo.tyabo.repository

interface UserRepository {

    fun signIn(): Result<String>
}