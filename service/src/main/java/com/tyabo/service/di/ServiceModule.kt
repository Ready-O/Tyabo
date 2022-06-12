package com.tyabo.service.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tyabo.service.implemetations.ChefDataSourceImpl
import com.tyabo.service.interfaces.FirebaseAuthDataSource
import com.tyabo.service.implemetations.FirebaseAuthDataSourceImpl
import com.tyabo.service.interfaces.ChefDataSource
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

    @Binds
    fun bindsChefDataSource(
        chefDataSource: ChefDataSourceImpl
    ): ChefDataSource

    companion object {
        @Provides
        @Singleton
        fun providesFirebaseAuth(): FirebaseAuth {
            return FirebaseAuth.getInstance()
        }

        @Provides
        @Singleton
        fun providesFirestore(): FirebaseFirestore {
            return Firebase.firestore
        }
    }
}