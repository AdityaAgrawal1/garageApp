package com.example.garageapp.main.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.garageapp.main.db.dao.CarDao
import com.example.garageapp.main.db.dao.UserDao
import com.example.garageapp.main.db.entities.Car
import com.example.garageapp.main.db.entities.User


@Database(
    entities = [Car::class, User::class],
    version = 1,
    exportSchema = false
)

abstract class GarageDatabase : RoomDatabase(){
    abstract fun getUserDao(): UserDao
    abstract fun getCarDao():CarDao
}