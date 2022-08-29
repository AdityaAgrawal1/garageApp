package com.example.garageapp.cars.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.garageapp.base.BaseViewModel
import com.example.garageapp.cars.responses.CarMakeResponse
import com.example.garageapp.cars.responses.CarModelResponse
import com.example.garageapp.main.db.entities.Car
import com.example.garageapp.main.db.resources.DbResource
import com.example.garageapp.networks.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(private val carRepository: CarRepository) :
    BaseViewModel(carRepository) {

    private val _carModelResponse: MutableLiveData<Resource<CarModelResponse>> = MutableLiveData()
    val carModelResponse: LiveData<Resource<CarModelResponse>>
        get() = _carModelResponse

    private val _carMakeResponse: MutableLiveData<Resource<CarMakeResponse>> = MutableLiveData()
    val carMakeResponse: LiveData<Resource<CarMakeResponse>>
        get() = _carMakeResponse

    private val _carInsertDataStatus = MutableLiveData<DbResource<Long>>()
    val carInsertDataStatus: MutableLiveData<DbResource<Long>> = _carInsertDataStatus

    private val _carsData: MutableLiveData<DbResource<List<Car>>> = MutableLiveData()
    val carsData: LiveData<DbResource<List<Car>>>
        get() = _carsData

    fun getCarMakes() = viewModelScope.launch {
        _carMakeResponse.value = Resource.Loading
        val res = carRepository.getCarMakes()
        _carMakeResponse.value = res
    }

    /**
     * Performs an API call to the login service
     * */
    fun getCarModels(makeId:Int) = viewModelScope.launch {
        _carModelResponse.value = Resource.Loading
        val res = carRepository.getCarModels(makeId)
        _carModelResponse.value = res
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addCar(selectedCarMake:String, userId: Long, selectedCarModel: String) = viewModelScope.launch{
        _carInsertDataStatus.value = DbResource.Loading
        try {
            val res =  carRepository.upsertCar(selectedCarMake, selectedCarModel,userId)
            _carInsertDataStatus.postValue(DbResource.Success(res))
        } catch (exception: Exception) {
            _carInsertDataStatus.postValue(DbResource.Failure(4, exception.message!!))
        }
    }

     fun getAddedCars(userId: Long) = viewModelScope.launch {
        _carsData.value = DbResource.Loading
        try {
            val res =  carRepository.getAddedCars(userId)
            _carsData.postValue(DbResource.Success(res))
        } catch (exception: Exception) {
            _carsData.postValue(DbResource.Failure(3, exception.message!!))
        }
    }

    suspend fun removeCar(carId: String) {
        _carInsertDataStatus.value = DbResource.Loading
        try {
            val res = carRepository.removeCar(carId)
            _carInsertDataStatus.postValue(DbResource.Success(res))
        } catch (exception: Exception) {
            _carInsertDataStatus.postValue(DbResource.Failure(3, exception.message!!))
        }
    }

    suspend fun updateCarImage(carId: String, carImage:String) = carRepository.updateCarImage(carId,carImage)

    suspend fun logout(){
        carRepository.logout()
    }
}