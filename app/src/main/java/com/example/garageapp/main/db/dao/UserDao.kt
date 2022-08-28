package com.example.garageapp.main.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.garageapp.main.db.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: User) : Long

    @Query("SELECT * FROM users WHERE userName LIKE :userName AND password LIKE :password")
    fun loginUser(userName:String, password:String) : LiveData<User>

    @Query("select * from users where id Like :userId")
    fun getUserDataDetails(userId:Long) : LiveData<User>

    @Query("select * from users where userName Like :username")
    fun getUserByName(username:String) : LiveData<User>

    @Delete
    suspend fun clearUser(user: User)
}