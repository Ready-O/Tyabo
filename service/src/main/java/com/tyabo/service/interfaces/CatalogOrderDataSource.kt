package com.tyabo.service.interfaces

import com.tyabo.data.CatalogOrder
import com.tyabo.data.UserType

interface CatalogOrderDataSource {

    suspend fun updateCatalogOrder(
        catalogOrderId: String,
        catalogOrder: List<CatalogOrder>,
        userType: UserType,
        userId: String
    ): Result<Unit>

    suspend fun fetchCatalogOrder(
        catalogOrderId: String,
        userType: UserType,
        userId: String
    ): Result<List<CatalogOrder>>
}

