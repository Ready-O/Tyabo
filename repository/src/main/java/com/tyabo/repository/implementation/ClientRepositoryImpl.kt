package com.tyabo.repository.implementation

import com.tyabo.data.Client
import com.tyabo.persistence.cache.InMemoryClientCache
import com.tyabo.repository.interfaces.ClientRepository
import com.tyabo.service.firebase.interfaces.ClientDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val clientCache: InMemoryClientCache,
    private val clientDataSource: ClientDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : ClientRepository {

    override suspend fun addClient(client: Client) {
        withContext(ioDispatcher) {
            clientDataSource.addClient(client).onSuccess {
                clientCache.updateClient(client)
            }
        }
    }

    override fun getClient(clientId: String): Flow<Result<Client>> = flow {
        clientCache.getClient(clientId)
            .onSuccess {
                emit(Result.success(it))
            }
            .onFailure {
                clientDataSource.fetchClient(clientId)
                    .onSuccess { client ->
                        clientCache.updateClient(client)
                        emit(Result.success(client))
                    }
                    .onFailure {
                        emit(Result.failure<Client>(Exception()))
                    }
            }
    }.flowOn(ioDispatcher)
}