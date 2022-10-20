package com.tyabo.repository.implementation

import com.tyabo.common.UiResult
import com.tyabo.common.flatMap
import com.tyabo.data.*
import com.tyabo.persistence.cache.InMemoryChefCache
import com.tyabo.repository.interfaces.ChefRepository
import com.tyabo.service.firebase.interfaces.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ChefRepositoryImpl @Inject constructor(
    private val chefCache: InMemoryChefCache,
    private val chefDataSource: ChefDataSource,
    private val catalogOrderDataSource: CatalogOrderDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : ChefRepository {

    override suspend fun addNewChef(userId: String, name: String) {
        withContext(ioDispatcher){
            val generatedId = UUID.randomUUID().toString()
            val chef = Chef(
                id = userId,
                name = name,
                phoneNumber = "",
                chefPictureUrl = null,
                bannerPictureUrl = null,
                bio = null,
                catalogOrderId = generatedId
            )
            chefDataSource.updateChef(chef).onSuccess {
                catalogOrderDataSource.updateCatalogOrder(
                    catalogOrderId = chef.catalogOrderId,
                    catalogOrder = listOf(),
                    userType = UserType.Chef,
                    userId = chef.id
                ).onSuccess {
                    chefCache.updateChef(chef)
                }
            }
        }
    }

    override fun getChef(chefId: String): Flow<Result<Chef>> = flow {
        chefCache.getChef(chefId)
            .onSuccess {
                emit(Result.success(it))
            }
            .onFailure {
                chefDataSource.fetchChef(chefId)
                    .onSuccess { chef ->
                        chefCache.updateChef(chef)
                        emit(Result.success(chef))
                    }
                    .onFailure {
                        emit(Result.failure<Chef>(Exception()))
                    }
            }
    }.flowOn(ioDispatcher)

}