package com.tyabo.persistence.cache

import com.tyabo.data.Chef
import javax.inject.Inject

class InMemoryChefCacheImpl @Inject constructor(
) : InMemoryChefCache {

    private val chefMap = LinkedHashMap<String, Chef>()

    override fun updateChef(chef: Chef) {
        chefMap[chef.id] = chef
    }

    override fun getChef(chefId: String): Result<Chef> {
        val item = chefMap[chefId]
        return if (item != null) Result.success(item) else Result.failure(NoSuchElementException())
    }
}