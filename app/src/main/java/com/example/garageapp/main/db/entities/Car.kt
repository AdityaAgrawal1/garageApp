package com.example.garageapp.main.db.entities

import androidx.annotation.NonNull
import androidx.room.*
import com.example.garageapp.utils.DbConstants
import java.io.Serializable

@Entity(
    tableName = DbConstants.CARS
)
data class Car constructor(

    @PrimaryKey
    @NonNull
    val id: String,

    @Update(onConflict = OnConflictStrategy.IGNORE)
    val make: String? = null,

    val model: String? = null,

    var carImage: String? = null,

    val description: String? = null,

    val owner: Long? = null,

    @Update(onConflict = OnConflictStrategy.IGNORE)
    val createdAt: String? = null,

    val updatedAt: String? = null

): Serializable
