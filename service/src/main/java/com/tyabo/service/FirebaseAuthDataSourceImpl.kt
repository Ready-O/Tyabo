package com.tyabo.service

import com.google.firebase.auth.AdditionalUserInfo
import com.google.firebase.auth.FirebaseAuth
import com.tyabo.data.FirebaseUser

import com.tyabo.service.interfaces.FirebaseAuthDataSource
import javax.inject.Inject

class FirebaseAuthDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): FirebaseAuthDataSource {

    override fun getFirebaseUser(): Result<FirebaseUser> {
        val currentUser = firebaseAuth.currentUser ?: return Result.failure(Exception())
        val id = currentUser.uid
        val name = currentUser.displayName ?: "anonymous"
        return Result.success(FirebaseUser(id = id,name = name))
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}