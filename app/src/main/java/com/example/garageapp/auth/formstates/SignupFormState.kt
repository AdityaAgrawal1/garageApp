package com.example.garageapp.auth.formstates

/**
 * Data validation state of the login form.
 */
data class SignupFormState(
    val userNameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)