package com.tyabo.repository.implementation

import com.tyabo.common.UiResult
import com.tyabo.common.flatMap
import com.tyabo.data.Chef
import com.tyabo.persistence.cache.InMemoryChefCache
import com.tyabo.repository.interfaces.ChefProfileRepository
import com.tyabo.service.firebase.interfaces.ChefDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChefProfileRepositoryImpl @Inject constructor(
    private val chefCache: InMemoryChefCache,
    private val chefDataSource: ChefDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : ChefProfileRepository {

    private val _chefFlow = MutableStateFlow<UiResult<Chef>>(UiResult.Loading)
    override val chefFlow: StateFlow<UiResult<Chef>> = _chefFlow.asStateFlow()

    override suspend fun updateStateChef(userId: String){
        withContext(ioDispatcher){
            chefCache.getChef(userId).onSuccess {
                _chefFlow.value = UiResult.Success(it)
            }
        }
    }
}