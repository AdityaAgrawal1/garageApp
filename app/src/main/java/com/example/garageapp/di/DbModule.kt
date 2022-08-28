package com.example.garageapp.di

import android.content.Context
import androidx.room.Room
import com.example.garageapp.main.db.GarageDatabase
import com.example.garageapp.main.db.dao.CarDao
import com.example.garageapp.main.db.dao.UserDao
import com.example.garageapp.utils.DbConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Singleton
    @Provides
    fun providesGarageDatabase(@ApplicationContext context: Context) : GarageDatabase{
        return Room.databaseBuilder(
            context,
            GarageDatabase::class.java,
            DbConstants.DB_NAME
        ).build()
    }

    @Provides
    fun providesUserDao(garageDatabase: GarageDatabase) : UserDao {
        return garageDatabase.getUserDao()
    }

    @Provides
    fun providesCarDao(garageDatabase: GarageDatabase) : CarDao {
        return garageDatabase.getCarDao()
    }


}