package com.tyabo.repository.interfaces

import com.tyabo.common.UiResult
import com.tyabo.data.Client
import kotlinx.coroutines.flow.Flow

interface ClientRepository {

    suspend fun addClient(client: Client)
    fun getClient(clientId: String): Flow<Result<Client>>
}