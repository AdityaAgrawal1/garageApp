package com.example.garageapp.utils

import android.view.View
import android.view.animation.TranslateAnimation
import androidx.fragment.app.Fragment
import com.example.garageapp.App
import com.example.garageapp.BuildConfig
import com.example.garageapp.R
import com.example.garageapp.main.Responses.ErrorResponse
import com.example.garageapp.networks.Resource
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonSyntaxException


fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.slideUp(duration: Int = 200) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
}

fun View.slideDown(duration: Int = 200) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
}


fun printDebug(s: String) {
    if (BuildConfig.DEBUG) {
        println("s = $s")
    }
}

fun View.snackBar(message: String, action: (() -> Unit)? = null) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackBar.setAction(R.string.retry) {
            it()
        }
    }
    snackBar.show()
}

fun Fragment.handleApiError(
    failure: Resource.Failure,
    retry: (() -> Unit)? = null
) {
    when {
        failure.errorCode == 401 || failure.errorCode == 190 || failure.errorCode == 191 -> {
            requireView().snackBar(
                resources.getString(R.string.checkInternetConnection),
                retry
            )
        }

        failure.isNetworkError ->
            requireView().snackBar(
                resources.getString(R.string.checkInternetConnection),
                retry
            )

        else ->  {
            var errorResponse : ErrorResponse?= null
            try{
                errorResponse = App.gson.fromJson(
                    failure.errorBody?.string(), ErrorResponse::class.java
                )
            }catch (e : JsonSyntaxException){
                e.printStackTrace()
            }
            errorResponse
                ?.message?.let { requireView().snackBar(it) }
                ?: kotlin.run {
                    requireView().snackBar(resources.getString(R.string.somethingWentWrong))
                }
        }
    }
}




