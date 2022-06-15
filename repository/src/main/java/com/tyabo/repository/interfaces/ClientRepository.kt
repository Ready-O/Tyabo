package com.tyabo.repository.interfaces

import com.tyabo.common.FlowResult
import com.tyabo.data.Chef
import com.tyabo.data.Client
import kotlinx.coroutines.flow.Flow

interface ClientRepository {

    suspend fun addClient(client: Client)
    suspend fun getClient(clientId: String): Flow<FlowResult<Client>>
}