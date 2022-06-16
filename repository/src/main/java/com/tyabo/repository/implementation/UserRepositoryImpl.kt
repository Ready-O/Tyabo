package com.tyabo.repository.implementation

import com.tyabo.data.*
import com.tyabo.persistence.cache.InMemoryUserCache
import com.tyabo.repository.interfaces.ChefRepository
import com.tyabo.repository.interfaces.ClientRepository
import com.tyabo.repository.interfaces.RestaurantRepository
import com.tyabo.repository.interfaces.UserRepository
import com.tyabo.service.interfaces.FirebaseAuthDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userCache: InMemoryUserCache,
    private val userDataSource: FirebaseAuthDataSource,
    private val clientRepository: ClientRepository,
    private val chefRepository: ChefRepository,
    private val restaurantRepository: RestaurantRepository
) : UserRepository
{
    override fun getFirebaseUser(): Result<FirebaseUser> {
        return userDataSource.getFirebaseUser()
    }

    override suspend fun checkUserType(userId: String): Flow<Result<UserType>> {

        val clientFlow = clientRepository.getClient(userId)
        val chefFlow = chefRepository.getChef(userId)
        val restaurantFlow = restaurantRepository.getRestaurant(userId)

        return combine(
            clientFlow,
            chefFlow,
            restaurantFlow
        ) { clientResult, chefResult, restaurantResult ->

            when {
                clientResult.isSuccess -> {
                    Result.success(UserType.Client)
                }
                chefResult.isSuccess -> {
                    Result.success(UserType.Chef)
                }
                restaurantResult.isSuccess -> {
                    Result.success(UserType.Restaurant)
                }
                else -> {
                    Result.failure(Exception())
                }
            }
        }
    }

    override suspend fun addUser(userId: String, name: String, userType: UserType) {
        when(userType){
            UserType.Client -> clientRepository.addClient(Client(id = userId, name = name))
            UserType.Chef -> chefRepository.addChef(Chef(id = userId, name = name))
            UserType.Restaurant -> restaurantRepository.addRestaurant(
                Restaurant(id = userId, name = name)
            )
        }
    }
}