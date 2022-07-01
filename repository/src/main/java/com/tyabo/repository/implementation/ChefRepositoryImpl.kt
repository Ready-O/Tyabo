package com.tyabo.repository.implementation

import com.tyabo.common.UiResult
import com.tyabo.data.Chef
import com.tyabo.data.Menu
import com.tyabo.data.UserType
import com.tyabo.persistence.cache.InMemoryChefCache
import com.tyabo.repository.interfaces.ChefRepository
import com.tyabo.service.interfaces.ChefDataSource
import com.tyabo.service.interfaces.MenuDataSource
import com.tyabo.service.interfaces.MenuUploadSource
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
    private val menuUploadSource: MenuUploadSource,
    private val ioDispatcher: CoroutineDispatcher
) : ChefRepository {

    override suspend fun addChef(chef: Chef) {
        withContext(ioDispatcher){
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
            uploadMenuPicture(chefId = userId, menuId = menu.id, menuPictureUrl = menu.menuPictureUrl)
                .onSuccess { downloadUrl ->
                    val newMenu = menu.copy(menuPictureUrl = downloadUrl)
                    menuDataSource.addMenu(
                        menu = newMenu,
                        userType = UserType.Chef,
                        userId = userId
                    )
                        .onSuccess {
                            chefCache.updateMenu(chefId = userId, menu = newMenu)
                        }
                        .onFailure {
                            Timber.e("morty firestore")
                        }
                }
                .onFailure { Timber.e("morty upload") }
        }
    }

    private suspend fun uploadMenuPicture(
        chefId: String,
        menuId: String,
        menuPictureUrl: String?
    ): Result<String?> {
        return if (menuPictureUrl == null){
            Result.success(null)
        } else {
            menuUploadSource.uploadMenuPicture(
                userId = chefId,
                menuId = menuId,
                pictureUrl = menuPictureUrl,
                userType = UserType.Chef
            )
        }
    }

    override fun getMenus(chefId: String): Flow<UiResult<List<Menu>>> = flow {
        emit(UiResult.Loading)
        val list = mutableListOf<String>(
            "600290c0-50c1-4afc-87d8-eea1ec7733de",
            "3a296847-69b3-4fff-9fe6-88a19196392a",
            "40c39b5f-26b7-473f-89b3-4394a94945a4"
        )
        chefCache.getMenus(chefId)
            .onSuccess { emit(UiResult.Success(it)) }
            .onFailure {
                menuDataSource.fetchMenus(userType = UserType.Chef, userId = chefId, menusIds = list)
                    .onSuccess {  menus ->
                        menus.forEach {
                            chefCache.updateMenu(chefId = chefId, menu = it)
                        }
                        emit(UiResult.Success(menus))
                    }
                    .onFailure {
                        emit(UiResult.Failure(Exception()))
                    }
            }
    }.flowOn(ioDispatcher)
}