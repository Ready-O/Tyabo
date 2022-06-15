package com.tyabo.repository.implementation

import com.tyabo.common.FlowResult
import com.tyabo.data.Chef
import com.tyabo.data.FirebaseUser
import com.tyabo.data.UserType
import com.tyabo.persistence.cache.InMemoryUserCache
import com.tyabo.repository.interfaces.ChefRepository
import com.tyabo.repository.interfaces.UserRepository
import com.tyabo.service.interfaces.FirebaseAuthDataSource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userCache: InMemoryUserCache,
    private val userDataSource: FirebaseAuthDataSource,
    private val chefRepository: ChefRepository
) : UserRepository
{
    override fun getFirebaseUser(): Result<FirebaseUser> {
        return userDataSource.getFirebaseUser()
    }

    override suspend fun checkUserType(userId: String): Result<UserType> {
        updateUser(userId)
        return userCache.getUserType()
    }

    private suspend fun updateUser(userId: String){
        chefRepository.getChef(userId).collectLatest { flowResult ->
            when (flowResult) {
                is FlowResult.Success -> {userCache.updateUser(userId = userId, userType = UserType.Chef)}
                else -> {}
            }
        }
    }

    override suspend fun addUser(userId: String, name: String, userType: UserType) {
        when(userType){
            UserType.Chef -> chefRepository.addChef(Chef(id = userId, name = name))
        }
    }
}