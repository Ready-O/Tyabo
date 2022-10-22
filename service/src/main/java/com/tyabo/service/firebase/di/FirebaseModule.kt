package com.tyabo.service.firebase.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.tyabo.service.firebase.di.CollectionReferences.CHEFS
import com.tyabo.service.firebase.di.CollectionReferences.CLIENTS
import com.tyabo.service.firebase.di.CollectionReferences.RESTAURANTS
import com.tyabo.service.firebase.implemetations.*
import com.tyabo.service.firebase.interfaces.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface FirebaseModule {

    @Binds
    fun bindsFirebaseAuthDataSource(
        firebaseAuthDataSource: FirebaseAuthDataSourceImpl
    ): FirebaseAuthDataSource

    @Binds
    fun bindsMenuDataSource(
        menuDataSource: MenuDataSourceImpl
    ): MenuDataSource

    @Binds
    fun bindsMenuUploadSource(
        menuUploadSource: MenuUploadSourceImpl
    ): MenuUploadSource

    @Binds
    fun bindsCollectionDataSource(
        collectionDataSource: CollectionDataSourceImpl
    ): CollectionDataSource

    @Binds
    fun bindsCatalogOrderDataSource(
        catalogOrderDataSource: CatalogOrderDataSourceImpl
    ): CatalogOrderDataSource

    @Binds
    fun bindsChefUploadDataSource(
        chefUploadDataSource: ChefUploadDataSourceImpl
    ): ChefUploadDataSource

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
        fun providesStorageRef(): StorageReference {
            return Firebase.storage.reference
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