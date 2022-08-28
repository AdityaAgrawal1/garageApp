package com.example.garageapp.di

import android.content.Context
import com.example.garageapp.utils.UserLoginPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Singleton
    @Provides
    fun providesUserLoginPreferences(@ApplicationContext context: Context) : UserLoginPreferences{
        return UserLoginPreferences(context)
    }
}