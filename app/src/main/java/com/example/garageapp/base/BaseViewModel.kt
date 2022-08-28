package com.example.garageapp.base

import androidx.lifecycle.ViewModel

/**
 * All the ActivityViewModels & FragmentViewModels will extends this class
 * */
abstract class BaseViewModel(private val baseRepository: BaseRepository) : ViewModel() {

}