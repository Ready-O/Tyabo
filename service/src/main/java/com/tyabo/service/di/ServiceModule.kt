package com.tyabo.service.di

import com.google.firebase.auth.FirebaseAuth
import com.tyabo.service.interfaces.UserDataSource
import com.tyabo.service.UserDataSourceImpl
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