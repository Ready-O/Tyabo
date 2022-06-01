package com.tyabo.repository.interfaces

import com.tyabo.data.FirebaseUser

interface UserRepository {

    fun getFirebaseUser(): Result<FirebaseUser>
}