package com.example.garageapp.cars.responses

import com.google.gson.annotations.SerializedName

data class CarModelResponse(

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Results")
	val results: List<ModelItem?>? = null,

	@field:SerializedName("Count")
	val count: Int? = null,

	@field:SerializedName("SearchCriteria")
	val searchCriteria: String? = null
)

data class ModelItem(

	@field:SerializedName("Make_ID")
	val makeID: Int? = null,

	@field:SerializedName("Model_ID")
	val modelID: Int? = null,

	@field:SerializedName("Make_Name")
	val makeName: String? = null,

	@field:SerializedName("Model_Name")
	val modelName: String? = null
)
