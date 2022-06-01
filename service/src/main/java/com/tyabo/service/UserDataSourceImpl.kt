package com.tyabo.service

import com.google.firebase.auth.FirebaseAuth
import com.tyabo.data.FirebaseUser

import com.tyabo.service.interfaces.UserDataSource
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): UserDataSource {

    override fun getFirebaseUser(): Result<FirebaseUser> {
        val currentUser = firebaseAuth.currentUser ?: return Result.failure(Exception())
        val id = currentUser.uid
        val name = currentUser.displayName ?: "anonymous"
        return Result.success(FirebaseUser(id = id,name = name))
    }
}