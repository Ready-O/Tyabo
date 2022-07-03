package com.tyabo.persistence.di

import com.tyabo.data.*
import com.tyabo.data.Collection
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
                collectionMap = LinkedHashMap<String, LinkedHashMap<String, Collection>>(),
                catalogOrder = LinkedHashMap<String, List<CatalogOrder>>()
            )

        @Provides
        @Singleton
        fun provideRestaurantCache(): InMemoryRestaurantCache =
            InMemoryRestaurantCacheImpl(restaurantMap = LinkedHashMap<String, Restaurant>())
    }


}