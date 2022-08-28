package com.tyabo.repository.interfaces

import com.tyabo.common.UiResult
import com.tyabo.data.Collection
import kotlinx.coroutines.flow.Flow

interface ChefCollectionRepository {

    suspend fun addCollection(collectionName: String, userId: String): Result<String>
    suspend fun editCollection(collectionId: String, collectionName: String, userId: String)
    suspend fun deleteCollection(collectionId: String, userId: String): Result<Unit>
    fun getCollections(userId: String, collectionsIds: List<String>): Flow<Result<List<Collection>>>

}