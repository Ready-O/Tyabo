package com.tyabo.repository.interfaces

import com.tyabo.common.FlowResult
import com.tyabo.data.Restaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {

    suspend fun addRestaurant(restaurant: Restaurant)
    suspend fun getRestaurant(restaurantId: String): Flow<FlowResult<Restaurant>>
}