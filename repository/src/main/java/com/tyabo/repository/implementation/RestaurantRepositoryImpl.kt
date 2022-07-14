package com.tyabo.repository.implementation

import com.tyabo.data.Restaurant
import com.tyabo.persistence.cache.InMemoryRestaurantCache
import com.tyabo.repository.interfaces.RestaurantRepository
import com.tyabo.service.firebase.interfaces.RestaurantDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val restaurantCache: InMemoryRestaurantCache,
    private val restaurantDataSource: RestaurantDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : RestaurantRepository {

    override suspend fun addRestaurant(restaurant: Restaurant) {
        withContext(ioDispatcher){
            restaurantDataSource.addRestaurant(restaurant).onSuccess {
                restaurantCache.updateRestaurant(restaurant)
            }
        }
    }

    override fun getRestaurant(restaurantId: String): Flow<Result<Restaurant>> = flow {
        restaurantCache.getRestaurant(restaurantId).onSuccess {
            emit(Result.success(it))
        }
            .onFailure {
                restaurantDataSource.fetchRestaurant(restaurantId).onSuccess { restaurant ->
                    restaurantCache.updateRestaurant(restaurant)
                    emit(Result.success(restaurant))
                }
                    .onFailure {
                        emit(Result.failure<Restaurant>(Exception()))
                    }
            }
    }.flowOn(ioDispatcher)

}