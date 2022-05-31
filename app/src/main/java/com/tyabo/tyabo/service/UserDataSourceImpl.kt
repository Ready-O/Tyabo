package com.tyabo.tyabo.service

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): UserDataSource {

    override fun signIn(): Result<String> {
        val currentUser = firebaseAuth.currentUser
        val name = (currentUser?.displayName ?: "")
        return Result.success(name)
    }
}