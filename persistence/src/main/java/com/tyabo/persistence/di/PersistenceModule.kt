package com.tyabo.persistence.di

import com.tyabo.persistence.interfaces.SessionDataStore
import com.tyabo.persistence.implementation.SessionPreferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PersistenceModule {

    @Binds
    fun bindsSessionDataStore(
        sessionDataStore: SessionPreferences
    ): SessionDataStore
}