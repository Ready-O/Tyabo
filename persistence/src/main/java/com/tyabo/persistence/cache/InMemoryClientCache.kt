package com.tyabo.persistence.cache

import com.tyabo.common.FlowResult
import com.tyabo.data.Chef
import com.tyabo.data.Client
import kotlinx.coroutines.flow.Flow

interface InMemoryClientCache {

    fun updateClient(client: Client)
    fun getClient(clientId: String): Result<Client>
}