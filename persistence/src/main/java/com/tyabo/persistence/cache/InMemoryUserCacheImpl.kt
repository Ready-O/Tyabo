package com.tyabo.persistence.cache

import com.tyabo.data.UserType
import javax.inject.Inject

class InMemoryUserCacheImpl @Inject constructor(
    private var cachedUserId: String,
    private var cachedUserType: UserType?
) : InMemoryUserCache {

    override fun updateUser(userId: String, userType: UserType) {
        cachedUserId = userId
        cachedUserType = userType
    }

    override fun getUserId(): String {
        return cachedUserId
    }

    override fun getUserType(): UserType {
        return cachedUserType!!
    }
}