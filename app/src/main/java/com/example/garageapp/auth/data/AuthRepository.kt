package com.example.garageapp.auth.data
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.garageapp.base.BaseRepository
import com.example.garageapp.main.db.GarageDatabase
import com.example.garageapp.main.db.entities.User
import com.example.garageapp.utils.DateTimeHelper.Companion.getCurrentDateAndTime
import javax.inject.Inject

class AuthRepository @Inject constructor(
    val db : GarageDatabase
): BaseRepository() {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun upsertUser(userName: String, password: String): Long {
        val user = User(
            createdAt = getCurrentDateAndTime(),
            updatedAt = getCurrentDateAndTime(),
            userName = userName,
            profileImage = "",
            password = password
        )
        return db.getUserDao().upsertUser(user)
    }

    suspend fun loginUser(userName:String, password:String): User {
        return db.getUserDao().loginUser(userName,password)
    }

    suspend fun getUserData(userId:Long): User {
        return db.getUserDao().getUserDataDetails(userId)
    }

    suspend fun checkIfUserExists(username:String): User {
        return db.getUserDao().getUserByName(username)
    }

}