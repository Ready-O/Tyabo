package com.tyabo.repository.di

import com.tyabo.repository.interfaces.SessionRepository
import com.tyabo.repository.implementation.SessionRepositoryImpl
import com.tyabo.repository.interfaces.UserRepository
import com.tyabo.repository.implementation.UserRepositoryImpl
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