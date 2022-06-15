package com.tyabo.repository.implementation

import com.tyabo.common.FlowResult
import com.tyabo.data.Restaurant
import com.tyabo.persistence.cache.InMemoryRestaurantCache
import com.tyabo.repository.interfaces.RestaurantRepository
import com.tyabo.service.interfaces.RestaurantDataSource
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
            restaurantDataSource.addRestaurant(restaurant)
        }
    }

    override suspend fun getRestaurant(restaurantId: String): Flow<FlowResult<Restaurant>> = flow {
        emit(FlowResult.Loading)
        restaurantCache.getRestaurant(restaurantId).onSuccess {
            emit(FlowResult.Success(it))
        }
            .onFailure {
                restaurantDataSource.fetchRestaurant(restaurantId).onSuccess { restaurant ->
                    restaurantCache.updateRestaurant(restaurant)
                    emit(FlowResult.Success(restaurant))
                }
                    .onFailure {
                        emit(FlowResult.Failure(Exception()))
                    }
            }
    }.flowOn(ioDispatcher)

}