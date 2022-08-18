package com.tyabo.tyabo.di

import com.tyabo.tyabo.ui.AppPresenter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun providesAppPresenter() = AppPresenter()
}