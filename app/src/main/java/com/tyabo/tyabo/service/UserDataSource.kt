package com.tyabo.tyabo.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

interface UserDataSource {

    fun getFirebaseUser(): Result<FirebaseUser>
}