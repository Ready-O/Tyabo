package com.tyabo.repository.implementation

import com.tyabo.common.flatMap
import com.tyabo.data.Collection
import com.tyabo.data.UserType
import com.tyabo.persistence.cache.InMemoryChefCache
import com.tyabo.repository.interfaces.ChefCollectionRepository
import com.tyabo.service.firebase.interfaces.CollectionDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*
import javax.inject.Inject

class ChefCollectionRepositoryImpl @Inject constructor(
    private val chefCache: InMemoryChefCache,
    private val collectionDataSource: CollectionDataSource,
    private val ioDispatcher: CoroutineDispatcher
): ChefCollectionRepository {

    override suspend fun addCollection(collectionName: String, userId: String): Result<String> {
        val generatedId = UUID.randomUUID().toString()
        val collection = Collection(id = generatedId, name = collectionName)
        return collectionDataSource.editCollection(
            collection = collection,
            userType = UserType.Chef,
            userId = userId
        )
            .flatMap {
                chefCache.updateCollection(chefId = userId, collection = collection)
                Result.success(generatedId)
            }
    }

    override suspend fun editCollection(
        collectionId: String,
        collectionName: String,
        userId: String
    ) {
        val collection = Collection(id = collectionId, name = collectionName)
        collectionDataSource.editCollection(
            collection = collection,
            userType = UserType.Chef,
            userId = userId
        )
            .onSuccess {
                chefCache.updateCollection(chefId = userId, collection = collection)
            }
    }

    override suspend fun deleteCollection(collectionId: String, userId: String): Result<Unit> {
        return collectionDataSource.deleteCollection(
            collectionId = collectionId,
            userId = userId,
            userType = UserType.Chef
        ).flatMap {
            chefCache.deleteCollection(
                chefId = userId,
                collectionId = collectionId
            )
            Result.success(Unit)
        }
    }

    override fun getCollections(
        userId: String,
        collectionsIds: List<String>
    ): Flow<Result<List<Collection>>> = flow{
        chefCache.getCollections(userId)
            .onSuccess { emit(Result.success(it)) }
            .onFailure {
                collectionDataSource.fetchCollections(userType = UserType.Chef, userId = userId, collectionsIds = collectionsIds)
                    .onSuccess {  collections ->
                        collections.forEach {
                            chefCache.updateCollection(chefId = userId, collection = it)
                        }
                        emit(Result.success(collections))
                    }
                    .onFailure {
                        emit(Result.failure<List<Collection>>(Exception()))
                    }
            }
    }.flowOn(ioDispatcher)
}