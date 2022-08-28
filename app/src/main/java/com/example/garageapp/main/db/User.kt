package com.example.garageapp.main.db

import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Update
import com.example.garageapp.utils.DbConstants
import java.io.Serializable

@Entity(
    tableName = DbConstants.USERS
)
data class User(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @Update(onConflict = OnConflictStrategy.IGNORE)
    val createdAt: String? = null,

    val updatedAt: String? = null,

    val userName: String? = null,

    val profileImage: String? = null,

    val password: String? = null

): Serializable
