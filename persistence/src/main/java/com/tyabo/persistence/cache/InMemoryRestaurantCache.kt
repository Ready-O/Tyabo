package com.tyabo.persistence.cache

import com.tyabo.data.Restaurant

interface InMemoryRestaurantCache {

    fun updateRestaurant(restaurant: Restaurant)
    fun getRestaurant(restaurantId: String): Result<Restaurant>
}