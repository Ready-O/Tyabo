package com.tyabo.service.firebase.interfaces


import com.tyabo.data.FirebaseUser

interface FirebaseAuthDataSource {

    fun getFirebaseUser(): Result<FirebaseUser>
    fun signOut()
}