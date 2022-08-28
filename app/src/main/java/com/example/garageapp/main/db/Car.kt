package com.example.garageapp.main.db

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Update
import com.example.garageapp.utils.DateTimeHelper
import com.example.garageapp.utils.DbConstants
import java.io.Serializable

@Entity(
    tableName = DbConstants.CARS
)
data class Car constructor(

    @PrimaryKey
    @NonNull
    val id: String,

    val make: String? = null,

    val model: String? = null,

    val carImage: String? = null,

    val description: String? = null,

    val owner: Long? = null,

    @Update(onConflict = OnConflictStrategy.IGNORE)
    val createdAt: String? = null,

    val updatedAt: String? = null

): Serializable
