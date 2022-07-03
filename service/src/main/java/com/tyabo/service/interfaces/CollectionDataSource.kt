package com.tyabo.service.interfaces

import com.tyabo.data.CatalogOrder
import com.tyabo.data.Collection
import com.tyabo.data.Menu
import com.tyabo.data.UserType

interface CollectionDataSource {
    suspend fun addCollection(collection: Collection, userType: UserType, userId: String): Result<Unit>
    suspend fun fetchCollections(userType: UserType, userId: String, catalogToFetch: List<CatalogOrder>): Result<List<Collection>>
}