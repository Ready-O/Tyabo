package com.tyabo.repository.implementation

import com.tyabo.common.FlowResult
import com.tyabo.data.*
import com.tyabo.persistence.cache.InMemoryUserCache
import com.tyabo.repository.interfaces.ChefRepository
import com.tyabo.repository.interfaces.ClientRepository
import com.tyabo.repository.interfaces.RestaurantRepository
import com.tyabo.repository.interfaces.UserRepository
import com.tyabo.service.interfaces.FirebaseAuthDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import timber.log.Timber
import java.lang.Exception
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

    override suspend fun checkUserType(userId: String): Flow<FlowResult<UserType>> {

        val clientFlow = clientRepository.getClient(userId)
        val chefFlow = chefRepository.getChef(userId)
        val restaurantFlow = restaurantRepository.getRestaurant(userId)

        return combine(
            clientFlow,
            chefFlow,
            restaurantFlow
        ) { clientResult, chefResult, restaurantResult ->

            when (clientResult) {
                is FlowResult.Success -> FlowResult.Success(UserType.Client)
                is FlowResult.Loading -> FlowResult.Loading
                is FlowResult.Failure -> {
                    when (chefResult) {
                        is FlowResult.Success -> FlowResult.Success(UserType.Chef)
                        is FlowResult.Loading -> FlowResult.Loading
                        is FlowResult.Failure ->
                            when (restaurantResult){
                                is FlowResult.Success -> FlowResult.Success(UserType.Restaurant)
                                is FlowResult.Loading -> FlowResult.Loading
                                is FlowResult.Failure -> FlowResult.Failure(Exception())
                            }
                    }
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