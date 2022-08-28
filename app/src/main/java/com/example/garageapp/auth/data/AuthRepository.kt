package com.example.garageapp.auth.data
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.garageapp.base.BaseRepository
import com.example.garageapp.main.db.GarageDatabase
import com.example.garageapp.main.db.User
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

    fun loginUser(userName:String, password:String): LiveData<User> {
        return db.getUserDao().loginUser(userName,password)
    }

    fun getUserData(userId:Long):LiveData<User> {
        return db.getUserDao().getUserDataDetails(userId)
    }

    fun checkIfUserExists(username:String):LiveData<User> {
        return db.getUserDao().getUserByName(username)
    }

}