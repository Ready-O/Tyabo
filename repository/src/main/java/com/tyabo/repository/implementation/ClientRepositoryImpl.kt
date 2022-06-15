package com.tyabo.repository.implementation

import com.tyabo.common.FlowResult
import com.tyabo.data.Client
import com.tyabo.persistence.cache.InMemoryClientCache
import com.tyabo.repository.interfaces.ClientRepository
import com.tyabo.service.interfaces.ClientDataSource
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
        withContext(ioDispatcher){
            clientDataSource.addClient(client)
        }
    }

    override suspend fun getClient(clientId: String): Flow<FlowResult<Client>> = flow {
        emit(FlowResult.Loading)
        clientCache.getClient(clientId).onSuccess {
            emit(FlowResult.Success(it))
        }
        .onFailure {
            clientDataSource.fetchClient(clientId).onSuccess { client ->
                clientCache.updateClient(client)
                emit(FlowResult.Success(client))
            }
                .onFailure {
                    emit(FlowResult.Failure(Exception()))
                }
        }
    }.flowOn(ioDispatcher)
}