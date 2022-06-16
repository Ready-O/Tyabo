package com.tyabo.persistence.cache

import com.tyabo.data.Client

interface InMemoryClientCache {

    fun updateClient(client: Client)
    fun getClient(clientId: String): Result<Client>
}