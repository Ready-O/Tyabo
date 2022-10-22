package com.tyabo.repository.implementation

import com.tyabo.common.UiResult
import com.tyabo.common.flatMap
import com.tyabo.data.Chef
import com.tyabo.persistence.cache.InMemoryChefCache
import com.tyabo.repository.interfaces.ChefProfileRepository
import com.tyabo.service.firebase.interfaces.ChefDataSource
import com.tyabo.service.firebase.interfaces.ChefUploadDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChefProfileRepositoryImpl @Inject constructor(
    private val chefCache: InMemoryChefCache,
    private val chefDataSource: ChefDataSource,
    private val chefUploadSource: ChefUploadDataSource,
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

    override suspend fun editChef(
        userId: String,
        name: String,
        phoneNumber: String,
        chefPictureUrl: String?,
        businessPictureUrl: String?,
        bio: String
    ) {
        withContext(ioDispatcher){
            val chefResult = chefCache.getChef(userId).flatMap {
                val chef = it.copy(
                    name = name,
                    phoneNumber = phoneNumber,
                    bio = bio
                )
                manageImageUploads(
                    oldChef = chef,
                    chefPicture = chefPictureUrl,
                    businessPicture = businessPictureUrl
                )
            }
            chefResult.onSuccess { chef ->
                chefDataSource.updateChef(chef).onSuccess {
                    chefCache.updateChef(chef)
                    _chefFlow.value = UiResult.Success(chef)
                }
            }
        }
    }

    // The logic is not that good but this part will be moved to backend anyway
    private suspend fun manageImageUploads(
        oldChef: Chef,
        chefPicture: String?,
        businessPicture: String?
    ): Result<Chef> {
        val userId = oldChef.id
        val oldChefPicture = oldChef.chefPictureUrl
        val newChefPicture =
            if (oldChefPicture != chefPicture) {
                chefUploadSource.deleteChefPicture(userId)
                if (chefPicture == null) {
                    Result.success(null)
                } else {
                    chefUploadSource.uploadChefPicture(
                        userId = userId,
                        pictureUrl = chefPicture
                    )
                }
            }
            else Result.success(oldChefPicture)
        val oldBusinessPicture = oldChef.businessPictureUrl
        val newBusinessPicture =
            if (oldBusinessPicture != businessPicture) {
                chefUploadSource.deleteBusinessPicture(userId)
                if (businessPicture == null) {
                    Result.success(null)
                } else {
                    chefUploadSource.uploadBusinessPicture(
                        userId = userId,
                        pictureUrl = businessPicture
                    )
                }
            }
            else Result.success(oldBusinessPicture)
        return if (newChefPicture.isSuccess && newBusinessPicture.isSuccess){
            Result.success(
                oldChef.copy(
                    chefPictureUrl = newChefPicture.getOrNull(),
                    businessPictureUrl = newBusinessPicture.getOrNull()
                )
            )
        }
        else {
            Result.failure(Exception())
        }
    }
}