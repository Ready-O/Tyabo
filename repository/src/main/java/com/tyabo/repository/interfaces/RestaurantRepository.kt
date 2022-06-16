package com.tyabo.repository.interfaces

import com.tyabo.common.UiResult
import com.tyabo.data.Restaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {

    suspend fun addRestaurant(restaurant: Restaurant)
    fun getRestaurant(restaurantId: String): Flow<Result<Restaurant>>
}