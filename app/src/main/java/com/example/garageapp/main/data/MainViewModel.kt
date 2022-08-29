package com.example.garageapp.main.data

import com.example.garageapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository)
    : BaseViewModel(repository) {

    companion object {
        @JvmStatic
        private val TAG = MainViewModel::class.java.canonicalName
    }

}