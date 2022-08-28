package com.example.garageapp.main.Responses

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @field:SerializedName("code") val code : Int?,
    @field:SerializedName("status") val status : String?,
    @field:SerializedName("message") val message : String?
)
