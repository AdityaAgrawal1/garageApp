package com.example.garageapp.main.data
import com.example.garageapp.base.BaseRepository
import com.example.garageapp.networks.ApiService
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService
): BaseRepository() {

    companion object {
        @JvmStatic
        private val TAG = MainRepository::class.java.canonicalName
    }

}

