package com.tyabo.persistence.cache

import com.tyabo.data.Restaurant
import javax.inject.Inject

class InMemoryRestaurantCacheImpl @Inject constructor(
    private val restaurantMap: LinkedHashMap<String, Restaurant>
) : InMemoryRestaurantCache {

    override fun updateRestaurant(restaurant: Restaurant) {
        restaurantMap[restaurant.id] = restaurant
    }

    override fun getRestaurant(restaurantId: String): Result<Restaurant> {
        val item = restaurantMap[restaurantId]
        return if (item != null) Result.success(item) else Result.failure(NoSuchElementException())
    }
}