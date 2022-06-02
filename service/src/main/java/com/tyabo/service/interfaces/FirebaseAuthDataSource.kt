package com.tyabo.service.interfaces


import com.tyabo.data.FirebaseUser

interface FirebaseAuthDataSource {

    fun getFirebaseUser(): Result<FirebaseUser>
    fun signOut()
}