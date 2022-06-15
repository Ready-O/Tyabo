package com.tyabo.service.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tyabo.service.di.CollectionReferences.CHEFS
import com.tyabo.service.di.CollectionReferences.CLIENTS
import com.tyabo.service.di.CollectionReferences.RESTAURANTS
import com.tyabo.service.implemetations.ChefDataSourceImpl
import com.tyabo.service.implemetations.ClientDataSourceImpl
import com.tyabo.service.interfaces.FirebaseAuthDataSource
import com.tyabo.service.implemetations.FirebaseAuthDataSourceImpl
import com.tyabo.service.implemetations.RestaurantDataSourceImpl
import com.tyabo.service.interfaces.ChefDataSource
import com.tyabo.service.interfaces.ClientDataSource
import com.tyabo.service.interfaces.RestaurantDataSource
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

        @Provides
        @Singleton
        fun providesFirestore(): FirebaseFirestore {
            return Firebase.firestore
        }

        @Provides
        @Singleton
        fun providesClientDataSource(
            firestore: FirebaseFirestore
        ): ClientDataSource = ClientDataSourceImpl(firestore.collection(CLIENTS))

        @Provides
        @Singleton
        fun providesChefDataSource(
            firestore: FirebaseFirestore
        ): ChefDataSource = ChefDataSourceImpl(firestore.collection(CHEFS))

        @Provides
        @Singleton
        fun providesRestaurantDataSource(
            firestore: FirebaseFirestore
        ): RestaurantDataSource = RestaurantDataSourceImpl(firestore.collection(RESTAURANTS))
    }
}