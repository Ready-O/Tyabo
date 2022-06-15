package com.tyabo.persistence.cache

import com.tyabo.common.FlowResult
import com.tyabo.data.Chef
import kotlinx.coroutines.flow.Flow

interface InMemoryChefCache {

    fun updateChef(chef: Chef)
    fun getChef(chefId: String): Result<Chef>
}