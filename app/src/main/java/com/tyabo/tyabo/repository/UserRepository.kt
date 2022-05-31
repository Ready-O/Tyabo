package com.tyabo.tyabo.repository

import com.google.firebase.auth.FirebaseUser

interface UserRepository {

    fun getFirebaseUser(): Result<FirebaseUser>
}