package com.example.garageapp.cars.responses

import com.google.gson.annotations.SerializedName

data class CarMakeResponse(

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Results")
	val results: List<MakeItem?>? = null,

	@field:SerializedName("Count")
	val count: Int? = null,

	@field:SerializedName("SearchCriteria")
	val searchCriteria: Any? = null
)

data class MakeItem(

	@field:SerializedName("Make_ID")
	val makeID: Int? = null,

	@field:SerializedName("Make_Name")
	val makeName: String? = null
)
