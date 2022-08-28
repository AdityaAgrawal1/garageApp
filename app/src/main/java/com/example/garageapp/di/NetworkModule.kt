package com.example.garageapp.di

import com.example.garageapp.networks.ApiService
import com.example.garageapp.networks.RetrofitHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun providesApiService(): ApiService {
        return RetrofitHelper().getService(ApiService::class.java)
    }
} 