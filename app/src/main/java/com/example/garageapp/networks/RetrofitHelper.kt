package com.example.garageapp.networks

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    private lateinit var retrofit: Retrofit

    init {
        getInstance()
    }

    private fun getInstance() : Retrofit {
        if (!this::retrofit.isInitialized) {
            synchronized(RetrofitHelper::class){
                if(!this::retrofit.isInitialized)
                    retrofit = Retrofit.Builder()
                        .baseUrl("https://vpic.nhtsa.dot.gov/api/vehicles/")
                        .client(provideOkHttpClient(provideLoggingInterceptor()))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
        }
        return retrofit
    }

    fun <T> getService(serviceClass: Class<T>): T {
        return getInstance().create(serviceClass)
    }

    private fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        val b = OkHttpClient.Builder()
        b.addInterceptor(interceptor)
        return b.build()
    }

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}