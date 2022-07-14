package com.tyabo.service.firebase.interfaces

import com.tyabo.data.Restaurant

interface RestaurantDataSource {

    fun addRestaurant(restaurant: Restaurant): Result<Unit>
    suspend fun fetchRestaurant(restaurantId: String): Result<Restaurant>
}