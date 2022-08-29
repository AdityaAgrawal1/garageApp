package com.example.garageapp.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.garageapp.R
import com.example.garageapp.main.data.MainViewModel
import com.example.garageapp.utils.UserLoginPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel

        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.main_navigation_graph)

        UserLoginPreferences(baseContext.applicationContext)
            .userId.asLiveData().observe(this){
                lifecycleScope.launch {
                    val homeId = if(it == null) R.id.loginFragment else R.id.carFragment
                    graph.setStartDestination(homeId)
                    navHostFragment.navController.graph = graph
                }
            }
    }
}