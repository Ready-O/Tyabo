package com.tyabo.service.firebase.interfaces

import com.tyabo.data.Collection
import com.tyabo.data.UserType

interface CollectionDataSource {
    suspend fun editCollection(collection: Collection, userType: UserType, userId: String): Result<Unit>
    suspend fun fetchCollections(userType: UserType, userId: String, collectionsIds: List<String>): Result<List<Collection>>
    suspend fun deleteCollection(collectionId: String, userId: String, userType: UserType): Result<Unit>
}