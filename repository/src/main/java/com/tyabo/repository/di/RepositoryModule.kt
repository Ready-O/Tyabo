package com.tyabo.repository.di

import com.tyabo.repository.implementation.*
import com.tyabo.repository.interfaces.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsUserRepository(
        userRepository: UserRepositoryImpl
    ): UserRepository

    @Binds
    fun bindsSessionRepository(
        sessionRepository: SessionRepositoryImpl
    ): SessionRepository

    @Binds
    fun bindsClientRepository(
        clientRepository: ClientRepositoryImpl
    ): ClientRepository

    @Binds
    fun bindsChefRepository(
        chefRepository: ChefRepositoryImpl
    ): ChefRepository

    @Binds
    @Singleton
    fun bindsChefCatalogRepository(
        chefCatalogRepository: ChefCatalogRepositoryImpl
    ): ChefCatalogRepository

    @Binds
    fun bindsChefMenuRepository(
        chefMenuRepository: ChefMenuRepositoryImpl
    ): ChefMenuRepository

    @Binds
    fun bindsChefCollectionRepository(
        chefCollectionRepository: ChefCollectionRepositoryImpl
    ): ChefCollectionRepository

    @Binds
    fun bindsRestaurantRepository(
        restaurantRepository: RestaurantRepositoryImpl
    ): RestaurantRepository

}