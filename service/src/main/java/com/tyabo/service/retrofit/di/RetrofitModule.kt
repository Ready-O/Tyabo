package com.tyabo.service.retrofit.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tyabo.service.retrofit.MenuVideoDataSource
import com.tyabo.service.retrofit.MenuVideoDataSourceImpl
import com.tyabo.service.retrofit.api.YoutubeApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RetrofitModule {

    @Binds
    @Singleton
    fun bindsMenuVideoDataSource(
        menuVideoDataSource: MenuVideoDataSourceImpl
    ): MenuVideoDataSource

    companion object {
        @Provides
        @Singleton
        fun providesMoshi(): Moshi {
            return Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        }

        @Provides
        @Singleton
        fun providesYoutubeApi(): YoutubeApi {
            return Retrofit.Builder()
                .baseUrl("https://youtube.com/")
                .addConverterFactory(MoshiConverterFactory.create(providesMoshi()))
                .build()
                .create(YoutubeApi::class.java)
        }
    }

}