package com.tyabo.service.interfaces


import com.tyabo.data.FirebaseUser

interface UserDataSource {

    fun getFirebaseUser(): Result<FirebaseUser>
}