package com.tyabo.persistence.di

import com.tyabo.data.Chef
import com.tyabo.data.Client
import com.tyabo.data.Menu
import com.tyabo.data.Restaurant
import com.tyabo.persistence.cache.*
import com.tyabo.persistence.datastore.SessionDataStore
import com.tyabo.persistence.datastore.SessionPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PersistenceModule {

    @Binds
    fun bindsSessionDataStore(
        sessionDataStore: SessionPreferences
    ): SessionDataStore

    /*
    @Binds
    fun bindsUserCache(
        inMemoryUserCache: InMemoryUserCacheImpl
    ): InMemoryUserCache

    @Binds
    fun bindsChefCache(
        inMemoryChefCache: InMemoryChefCacheImpl
    ): InMemoryChefCache

     */

    companion object{
        @Provides
        @Singleton
        fun provideUserCache(): InMemoryUserCache = InMemoryUserCacheImpl(cachedUserId = "", cachedUserType = null)

        @Provides
        @Singleton
        fun provideClientCache(): InMemoryClientCache = InMemoryClientCacheImpl(clientMap = LinkedHashMap<String, Client>())

        @Provides
        @Singleton
        fun provideChefCache(): InMemoryChefCache =
            InMemoryChefCacheImpl(
                chefMap = LinkedHashMap<String, Chef>(),
                menuMap = LinkedHashMap<String, LinkedHashMap<String, Menu>>(),
            )

        @Provides
        @Singleton
        fun provideRestaurantCache(): InMemoryRestaurantCache =
            InMemoryRestaurantCacheImpl(restaurantMap = LinkedHashMap<String, Restaurant>())
    }


}