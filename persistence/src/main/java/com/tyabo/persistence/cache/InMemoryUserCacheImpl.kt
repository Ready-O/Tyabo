package com.tyabo.persistence.cache

import com.tyabo.data.UserType
import javax.inject.Inject

class InMemoryUserCacheImpl @Inject constructor(
): InMemoryUserCache {

    private var cachedUserId: String = ""
    private var cachedUserType: UserType? = null

    override fun updateUser(userId: String, userType: UserType) {
        cachedUserId = userId
        cachedUserType = userType
    }

    override fun getUserId(): Result<String> {
        return if (cachedUserType != null) Result.success(cachedUserId) else Result.failure(NoSuchElementException())
    }

    override fun getUserType(): Result<UserType> {
        return if (cachedUserType != null) Result.success(cachedUserType!!) else Result.failure(NoSuchElementException())
    }
}