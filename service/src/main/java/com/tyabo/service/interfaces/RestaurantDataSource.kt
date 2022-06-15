package com.tyabo.service.interfaces

import com.tyabo.data.Chef
import com.tyabo.data.Restaurant

interface RestaurantDataSource {

    fun addRestaurant(restaurant: Restaurant)
    suspend fun fetchRestaurant(restaurantId: String): Result<Restaurant>
}