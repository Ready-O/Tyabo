package com.tyabo.repository.implementation

import com.tyabo.data.Chef
import com.tyabo.data.FirebaseUser
import com.tyabo.data.UserType
import com.tyabo.repository.interfaces.ChefRepository
import com.tyabo.repository.interfaces.UserRepository
import com.tyabo.service.interfaces.FirebaseAuthDataSource
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: FirebaseAuthDataSource,
    private val chefRepository: ChefRepository
) : UserRepository
{
    override fun getFirebaseUser(): Result<FirebaseUser> {
        return userDataSource.getFirebaseUser()
    }

    override suspend fun checkUserType(userId: String): Result<UserType> {
        chefRepository.getChef(userId).onSuccess {
            return Result.success(UserType.Chef)
        }.onFailure { Timber.e("mamacita") }
        return Result.failure(Exception())
    }

    override suspend fun addUser(userId: String, name: String, userType: UserType) {
        when(userType){
            UserType.Chef -> chefRepository.addChef(Chef(id = userId, name = name))
        }
    }
}