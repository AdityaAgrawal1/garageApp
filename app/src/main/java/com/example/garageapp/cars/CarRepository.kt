package com.example.garageapp.cars

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.garageapp.base.BaseRepository
import com.example.garageapp.cars.responses.CarMakeResponse
import com.example.garageapp.cars.responses.CarModelResponse
import com.example.garageapp.main.db.Car
import com.example.garageapp.main.db.GarageDatabase
import com.example.garageapp.networks.ApiService
import com.example.garageapp.networks.Resource
import com.example.garageapp.utils.DateTimeHelper.Companion.getCurrentDateAndTime
import com.example.garageapp.utils.UserLoginPreferences
import java.util.*
import javax.inject.Inject


class CarRepository @Inject constructor(
    private val apiService: ApiService,
    private val db: GarageDatabase,
    private val userLoginPreferences: UserLoginPreferences
) : BaseRepository() {

    suspend fun getCarMakes(): Resource<CarMakeResponse> = safeApiCall{
        apiService.getCarMakes()
    }

    suspend fun getCarModels(makeId:Int): Resource<CarModelResponse> = safeApiCall{
        apiService.getCarModels(makeId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun upsertCar(selectedCarMake:String, selectedCarModel: String, userId: Long) : Long {
        val currentTime = getCurrentDateAndTime()
        val car = Car(UUID.randomUUID().toString(), selectedCarMake,selectedCarModel,
            "","",userId,currentTime,currentTime)
       return db.getCarDao().upsertCar(car)
    }

    suspend fun getAddedCars(userId:Long): List<Car> {
        return db.getCarDao().getAllCars(userId)
    }

    suspend fun removeCar(carId:String){
        db.getCarDao().deleteCarById(carId)
    }


    suspend fun updateCarImage(carId:String, carImage:String) =  db.getCarDao().updateCarImage(carId, carImage)

    suspend fun logout(){
        userLoginPreferences.clear()
    }
}