package com.tyabo.persistence.cache

import com.tyabo.common.FlowResult
import com.tyabo.data.Chef
import com.tyabo.data.Restaurant
import kotlinx.coroutines.flow.Flow

interface InMemoryRestaurantCache {

    fun updateRestaurant(restaurant: Restaurant)
    fun getRestaurant(restaurantId: String): Result<Restaurant>
}