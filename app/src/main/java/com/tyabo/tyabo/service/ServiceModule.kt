package com.tyabo.tyabo.service

import com.tyabo.tyabo.repository.UserRepository
import com.tyabo.tyabo.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ServiceModule {

    @Binds
    fun bindsUserDataSource(
        userDataSource: UserDataSourceImpl
    ): UserDataSource
}