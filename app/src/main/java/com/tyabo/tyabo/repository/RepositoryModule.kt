package com.tyabo.tyabo.repository

import com.tyabo.tyabo.persistence.SessionDataStore
import com.tyabo.tyabo.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsUserRepository(
        userRepository: UserRepositoryImpl
    ): UserRepository

    @Binds
    fun bindsSessionRepository(
        sessionRepository: SessionRepositoryImpl
    ): SessionRepository
}