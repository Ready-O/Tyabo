package com.tyabo.tyabo.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): UserDataSource {

    override fun getFirebaseUser(): Result<FirebaseUser> {
        val currentUser = firebaseAuth.currentUser ?: return Result.failure(Exception())
        return Result.success(currentUser)
    }
}