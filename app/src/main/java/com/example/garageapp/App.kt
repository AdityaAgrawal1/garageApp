package com.example.garageapp

import android.app.Application
import com.google.gson.Gson
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App:Application() {

    companion object{
        private var _gson = Gson()
        val gson : Gson get() = _gson
    }
}