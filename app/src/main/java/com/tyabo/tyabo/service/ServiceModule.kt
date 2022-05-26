package com.tyabo.tyabo.service

import com.google.firebase.auth.FirebaseAuth
import com.tyabo.tyabo.repository.UserRepository
import com.tyabo.tyabo.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ServiceModule {

    @Binds
    fun bindsUserDataSource(
        userDataSource: UserDataSourceImpl
    ): UserDataSource

    companion object {
        @Provides
        @Singleton
        fun providesFirebaseAuth(): FirebaseAuth {
            return FirebaseAuth.getInstance()
        }
    }
}