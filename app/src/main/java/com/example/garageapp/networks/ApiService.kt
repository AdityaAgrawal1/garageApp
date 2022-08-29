package com.example.garageapp.networks
import com.example.garageapp.cars.data.responses.CarMakeResponse
import com.example.garageapp.cars.data.responses.CarModelResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("getallmakes?format=json")
    suspend fun getCarMakes() : CarMakeResponse

    @GET("GetModelsForMakeId/{make_id}?format=json")
    suspend fun getCarModels(
        @Path("make_id") makeId: Int,
    ) : CarModelResponse
}