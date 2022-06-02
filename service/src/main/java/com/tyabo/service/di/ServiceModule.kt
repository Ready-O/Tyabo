package com.tyabo.service.di

import com.google.firebase.auth.FirebaseAuth
import com.tyabo.service.interfaces.FirebaseAuthDataSource
import com.tyabo.service.FirebaseAuthDataSourceImpl
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
    fun bindsFirebaseAuthDataSource(
        firebaseAuthDataSource: FirebaseAuthDataSourceImpl
    ): FirebaseAuthDataSource

    companion object {
        @Provides
        @Singleton
        fun providesFirebaseAuth(): FirebaseAuth {
            return FirebaseAuth.getInstance()
        }
    }
}