package com.tyabo.persistence.cache

import com.tyabo.data.Chef
import com.tyabo.data.Client
import javax.inject.Inject

class InMemoryClientCacheImpl @Inject constructor(
    private val clientMap: LinkedHashMap<String, Client>
) : InMemoryClientCache {

    override fun updateClient(client: Client) {
        clientMap[client.id] = client
    }

    override fun getClient(clientId: String): Result<Client> {
        val item = clientMap[clientId]
        return if (item != null) Result.success(item) else Result.failure(NoSuchElementException())
    }
}