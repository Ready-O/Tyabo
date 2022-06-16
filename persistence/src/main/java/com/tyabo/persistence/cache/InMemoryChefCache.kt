package com.tyabo.persistence.cache

import com.tyabo.data.Chef

interface InMemoryChefCache {

    fun updateChef(chef: Chef)
    fun getChef(chefId: String): Result<Chef>
}