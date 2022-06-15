package com.tyabo.service.interfaces

import com.tyabo.data.Chef
import com.tyabo.data.Client

interface ClientDataSource {

    fun addClient(client: Client)
    suspend fun fetchClient(clientId: String): Result<Client>
}