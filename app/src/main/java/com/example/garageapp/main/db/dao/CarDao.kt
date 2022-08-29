package com.example.garageapp.main.db.dao

import androidx.room.*
import com.example.garageapp.main.db.entities.Car

@Dao
interface CarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCar(car: Car) : Long

    @Query("SELECT * FROM cars WHERE owner = :userId")
    suspend fun getAllCars(userId:Long) : List<Car>

    @Query("DELETE FROM cars WHERE id = :carId")
    suspend fun deleteCarById(carId: String):Int

    @Query("UPDATE cars SET carImage = :carImage WHERE id = :carId")
    suspend fun updateCarImage(carId: String, carImage:String)
}