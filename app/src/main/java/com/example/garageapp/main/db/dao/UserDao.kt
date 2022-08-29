package com.example.garageapp.main.db.dao

import androidx.room.*
import com.example.garageapp.main.db.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: User) : Long

    @Query("SELECT * FROM users WHERE userName LIKE :userName AND password LIKE :password")
    suspend fun loginUser(userName:String, password:String) : User

    @Query("select * from users where id Like :userId")
    suspend fun getUserDataDetails(userId:Long) : User

    @Query("select * from users where userName Like :username")
    suspend fun getUserByName(username:String) : User

    @Delete
    suspend fun clearUser(user: User)
}