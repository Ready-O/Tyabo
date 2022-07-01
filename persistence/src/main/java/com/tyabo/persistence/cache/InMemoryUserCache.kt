package com.tyabo.persistence.cache

import com.tyabo.data.UserType

interface InMemoryUserCache {

    fun updateUser(userId: String, userType: UserType)

    fun getUserId(): String

    fun getUserType(): UserType
}