package com.example.garageapp.main.db.resources

sealed class DbResource<out T> {
    data class Success<out T>(val value: T) : DbResource<T>()

    data class Failure(
        val errorCode: Int?,
        val errorMsg: String?
    ) : DbResource<Nothing>()

    object Loading : DbResource<Nothing>()
}