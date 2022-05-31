package com.tyabo.tyabo.persistence

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.tyabo.tyabo.repository.UserRepository
import com.tyabo.tyabo.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PersistenceModule {

    @Binds
    fun bindsSessionPreferences(
        sessionDataStore: SessionPreferences
    ): SessionDataStore
}