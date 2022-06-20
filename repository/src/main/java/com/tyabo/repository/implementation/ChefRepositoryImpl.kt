package com.tyabo.repository.implementation

import com.tyabo.data.Chef
import com.tyabo.data.Menu
import com.tyabo.data.UserType
import com.tyabo.persistence.cache.InMemoryChefCache
import com.tyabo.repository.interfaces.ChefRepository
import com.tyabo.service.interfaces.ChefDataSource
import com.tyabo.service.interfaces.MenuDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ChefRepositoryImpl @Inject constructor(
    private val chefCache: InMemoryChefCache,
    private val chefDataSource: ChefDataSource,
    private val menuDataSource: MenuDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : ChefRepository {

    override suspend fun addChef(chef: Chef) {
        withContext(ioDispatcher){
            chefDataSource.addChef(chef)
            chefDataSource.addChef(chef).onSuccess {
                chefCache.updateChef(chef)
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

    override suspend fun addMenu(menu: Menu, userId: String) {
        withContext(ioDispatcher){
            chefCache.updateMenu(chefId = userId, menu = menu)
            menuDataSource.addMenu(
                menu = menu,
                userType = UserType.Chef,
                userId = userId
            ).onFailure {
                chefCache.deleteMenu(chefId = userId, menuId = menu.id)
            }
        }
    }
}