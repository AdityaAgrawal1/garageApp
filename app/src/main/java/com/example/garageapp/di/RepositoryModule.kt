package com.example.garageapp.di

import com.example.garageapp.auth.data.AuthRepository
import com.example.garageapp.cars.CarRepository
import com.example.garageapp.main.MainRepository
import com.example.garageapp.main.db.GarageDatabase
import com.example.garageapp.networks.ApiService
import com.example.garageapp.utils.UserLoginPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesMainRepository(
        apiService: ApiService
    ) : MainRepository {
        return MainRepository(apiService)
    }

    @Provides
    fun providesCarRepository(
        apiService: ApiService,
        garageDB : GarageDatabase,
        userLoginPreferences: UserLoginPreferences
    ) : CarRepository {
        return CarRepository(apiService, garageDB,userLoginPreferences)
    }

    @Provides
    fun providesAuthRepository(
        garageDB : GarageDatabase
    ) : AuthRepository {
        return AuthRepository(garageDB)
    }
}