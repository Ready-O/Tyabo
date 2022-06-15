package com.tyabo.repository.implementation

import com.tyabo.common.FlowResult
import com.tyabo.data.Chef
import com.tyabo.persistence.cache.InMemoryChefCache
import com.tyabo.repository.interfaces.ChefRepository
import com.tyabo.service.interfaces.ChefDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChefRepositoryImpl @Inject constructor(
    private val chefCache: InMemoryChefCache,
    private val chefDataSource: ChefDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : ChefRepository {

    override suspend fun addChef(chef: Chef) {
        withContext(ioDispatcher){
            chefDataSource.addChef(chef)
        }
    }

    override suspend fun getChef(chefId: String): Flow<FlowResult<Chef>> = flow {
        emit(FlowResult.Loading)
        chefCache.getChef(chefId).onSuccess {
            emit(FlowResult.Success(it))
        }
        .onFailure {
            chefDataSource.fetchChef(chefId).onSuccess { chef ->
                chefCache.updateChef(chef)
                emit(FlowResult.Success(chef))
            }
                .onFailure {
                    emit(FlowResult.Failure(Exception()))
                }
        }
    }.flowOn(ioDispatcher)
}