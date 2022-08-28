package com.example.garageapp.cars.ui

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CarDetails(

	@field:SerializedName("car_id")
	val carId: String,

	@field:SerializedName("car_image")
    var carImage: String? = null,

	@field:SerializedName("make")
	val carMake: String? = null,

	@field:SerializedName("model")
	val carModel: String? = null

) : Serializable