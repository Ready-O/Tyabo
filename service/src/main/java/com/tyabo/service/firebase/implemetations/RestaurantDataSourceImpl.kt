package com.tyabo.service.firebase.implemetations

import com.google.firebase.firestore.CollectionReference
import com.tyabo.data.Restaurant
import com.tyabo.service.firebase.interfaces.RestaurantDataSource
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class RestaurantDataSourceImpl @Inject constructor(
    private val restaurantsCollection: CollectionReference
) : RestaurantDataSource {

    override fun addRestaurant(restaurant: Restaurant): Result<Unit> {
        return try {
            restaurantsCollection.document(restaurant.id).set(restaurant)
            Result.success(Unit)
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun fetchRestaurant(restaurantId: String): Result<Restaurant> {
        return try {
            val snapshot = restaurantsCollection.document(restaurantId).get().await()
            val restaurant = snapshot?.toObject(RemoteRestaurant::class.java)
            return if (restaurant == null) {
                Result.failure(Exception())
            } else {
                Result.success(restaurant.toRestaurant())
            }
        }
        catch (e: Exception){
            Result.failure(Exception())
        }
    }

    private data class RemoteRestaurant(
        var id: String = "",
        var name: String = ""
    )

    private fun RemoteRestaurant.toRestaurant() = Restaurant(
        id = this.id,
        name = this.name
    )

}