package com.tyabo.repository.implementation

import com.tyabo.common.UiResult
import com.tyabo.common.flatMap
import com.tyabo.data.CatalogItemType
import com.tyabo.data.Menu
import com.tyabo.data.MenuYoutubeVideo
import com.tyabo.data.UserType
import com.tyabo.persistence.cache.InMemoryChefCache
import com.tyabo.repository.interfaces.ChefMenuRepository
import com.tyabo.service.firebase.interfaces.MenuDataSource
import com.tyabo.service.firebase.interfaces.MenuUploadSource
import com.tyabo.service.retrofit.MenuVideoDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ChefMenuRepositoryImpl @Inject constructor(
    private val chefCache: InMemoryChefCache,
    private val menuDataSource: MenuDataSource,
    private val menuUploadSource: MenuUploadSource,
    private val menuVideoDataSource: MenuVideoDataSource,
    private val ioDispatcher: CoroutineDispatcher
): ChefMenuRepository {

    override suspend fun addMenu(menu: Menu, userId: String): Result<Unit> {
        return uploadMenuPicture(chefId = userId, menuId = menu.id, menuPictureUrl = menu.menuPictureUrl)
            .flatMap { downloadUrl ->
                val newMenu = menu.copy(menuPictureUrl = downloadUrl)
                updateMenuFirestore(menu = newMenu, chefId = userId)
            }
            .onFailure {
                menuUploadSource.deleteMenuPicture(
                    userId = userId,
                    menuId = menu.id,
                    userType = UserType.Chef
                )
            }
    }

    override suspend fun editMenu(menu: Menu, userId: String) {
        val oldMenu = chefCache.getMenus(userId).getOrThrow().find { it.id == menu.id }
        if(menu.menuPictureUrl != oldMenu!!.menuPictureUrl) {
            menuUploadSource.deleteMenuPicture(
                userId = userId,
                menuId = menu.id,
                userType = UserType.Chef
            )
            uploadMenuPicture(chefId = userId, menuId = menu.id, menuPictureUrl = menu.menuPictureUrl)
                .onSuccess { downloadUrl ->
                    val newMenu = menu.copy(menuPictureUrl = downloadUrl)
                    updateMenuFirestore(menu = newMenu, chefId = userId)
                        .onFailure {
                            menuUploadSource.deleteMenuPicture(
                                userId = userId,
                                menuId = menu.id,
                                userType = UserType.Chef
                            )
                        }
                }
        }
        else {
            updateMenuFirestore(menu = menu, chefId = userId)
        }
    }

    override suspend fun changeHideMenu(menuId: String, isHidden: Boolean, userId: String) {
        val menu = chefCache.getMenus(userId).getOrThrow().find { it.id == menuId }
        updateMenuFirestore(
            menu = menu!!.copy(isHidden = isHidden),
            chefId = userId
        )
    }

    override suspend fun deleteMenu(menuId: String, userId: String): Result<Unit> {
        menuUploadSource.deleteMenuPicture(
            userId = userId,
            menuId = menuId,
            userType = UserType.Chef
        )
        return menuDataSource.deleteMenu(
            menuId = menuId,
            userId = userId,
            userType = UserType.Chef
        ).flatMap {
            chefCache.deleteMenu(
                chefId = userId,
                menuId = menuId
            )
            Result.success(Unit)
        }
    }

    override fun getMenus(userId: String, menusIds: List<String>): Flow<Result<List<Menu>>> = flow {
        chefCache.getMenus(userId)
            .onSuccess { emit(Result.success(it)) }
            .onFailure {
                menuDataSource.fetchMenus(userType = UserType.Chef, userId = userId, menusIds = menusIds)
                    .onSuccess {  menus ->
                        menus.forEach {
                            chefCache.updateMenu(chefId = userId, menu = it)
                        }
                        emit(Result.success(menus))
                    }
                    .onFailure {
                        emit(Result.failure<List<Menu>>(Exception()))
                    }
            }
    }.flowOn(ioDispatcher)

    private suspend fun updateMenuFirestore(
        menu: Menu,
        chefId: String,
    ): Result<Unit> {
        return menuDataSource.editMenu(
            menu = menu,
            userType = UserType.Chef,
            userId = chefId
        )
            .onSuccess {
                chefCache.updateMenu(chefId = chefId, menu = menu)
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

    override suspend fun getVideo(url: String): Result<MenuYoutubeVideo> {
        return menuVideoDataSource.importVideo(url)
    }
}