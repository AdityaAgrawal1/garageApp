package com.example.garageapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

class DateTimeHelper {
    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        fun getCurrentDateAndTime() : String{
            return LocalDateTime.now().toString()}
        }
    }
