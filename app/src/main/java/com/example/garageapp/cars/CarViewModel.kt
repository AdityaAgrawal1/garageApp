package com.example.garageapp.cars

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.garageapp.base.BaseViewModel
import com.example.garageapp.cars.responses.*
import com.example.garageapp.main.db.Car
import com.example.garageapp.main.db.DbResource
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

    private val _carsData: MutableLiveData<DbResource<LiveData<List<Car>>>> = MutableLiveData()
    val carsData: LiveData<DbResource<LiveData<List<Car>>>>
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
    fun addCar(selectedCarMake:String, selectedCarModel: String) = viewModelScope.launch{
        carRepository.upsertCar(selectedCarMake, selectedCarModel)
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

    fun removeCar(carId: String) = viewModelScope.launch{
        carRepository.removeCar(carId)
    }

    suspend fun updateCarImage(carId: String, carImage:String) = carRepository.updateCarImage(carId,carImage)

    suspend fun logout(){
        carRepository.logout()
    }
}