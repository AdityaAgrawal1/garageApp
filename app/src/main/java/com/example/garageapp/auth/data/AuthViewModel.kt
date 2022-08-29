package com.example.garageapp.auth.data
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.garageapp.R
import com.example.garageapp.auth.formstates.LoginFormState
import com.example.garageapp.auth.formstates.SignupFormState
import com.example.garageapp.base.BaseViewModel
import com.example.garageapp.main.db.resources.DbResource
import com.example.garageapp.main.db.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject
@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository)
    : BaseViewModel(authRepository) {

    private val _insertUsersDataStatus = MutableLiveData<DbResource<Long>>()
    val insertUsersDataStatus: LiveData<DbResource<Long>> = _insertUsersDataStatus

    private val _userLoginDataStatus = MutableLiveData<DbResource<User>>()
    val userLoginDataStatus: MutableLiveData<DbResource<User>> = _userLoginDataStatus

    private val _userProfileDataStatus = MutableLiveData<DbResource<User>>()
    val userProfileDataStatus: MutableLiveData<DbResource<User>> = _userProfileDataStatus


    /** Holds the signup form state presented on screen*/
    private val _signupForm = MutableLiveData(SignupFormState())
    val signupFormState: LiveData<SignupFormState> = _signupForm

    /** Holds the login form state presented on screen*/
    private val _loginForm = MutableLiveData(LoginFormState())
    val loginFormState: LiveData<LoginFormState> = _loginForm

    @RequiresApi(Build.VERSION_CODES.O)
    fun signup(userName: String, password: String) = viewModelScope.launch {
        _insertUsersDataStatus.value = DbResource.Loading
        try {
            val res = authRepository.upsertUser(userName,password)
            _insertUsersDataStatus.postValue(DbResource.Success(res))
        } catch (exception: Exception) {
            _insertUsersDataStatus.postValue(DbResource.Failure(0, exception.message!!))
        }
    }

    fun getUserLoginDataStatus(userName: String, password: String) {
        viewModelScope.launch {
            _userLoginDataStatus.postValue(DbResource.Loading)
            try {
                val res = authRepository.loginUser(userName,password)
                _userLoginDataStatus.postValue(DbResource.Success(res))
            } catch (exception: Exception) {
                _userLoginDataStatus.postValue(DbResource.Failure(1, exception.message!!))
            }
        }
    }

    fun getUserData(userId:Long) {
        viewModelScope.launch {
            _userProfileDataStatus.postValue(DbResource.Loading)
            try {
                val res = authRepository.getUserData(userId)
                _userProfileDataStatus.postValue(DbResource.Success(res))
            } catch (exception: Exception) {
                _userProfileDataStatus.postValue(DbResource.Failure(2, exception.message!!))
            }
        }
    }


    /**
     * Whenever login data changes, this methods performs validations
     * @param password credentials of the user
     * */
    suspend fun signupDataChanged(userName: String, password: String)  = withContext(Dispatchers.IO){
        if (userName.isEmpty()) {
            _signupForm.postValue(SignupFormState(userNameError = R.string.user_name_must_not_be_empty))
        }
        else if (userName.length < 4 || userName.length > 10) {
            _signupForm.postValue(SignupFormState(userNameError = R.string.user_name_length))
        } else if (!isUserNameValid(userName)) {
            _signupForm.postValue(SignupFormState(userNameError = R.string.user_name_not_valid))
        }else if (checkIfUserExists(userName)!=null) {
            _signupForm.postValue(SignupFormState(userNameError = R.string.user_already_exists))
        }else if (!isPasswordValid(password)) {
            _signupForm.postValue(SignupFormState(passwordError = R.string.password_length))
        }else {
            _signupForm.postValue(SignupFormState(isDataValid = true))
        }
    }

    /**
     * Whenever login data changes, this methods performs validations
     * @param userName user name of the user
     * @param password credentials of the user
     * */
    suspend fun loginDataChanged(userName: String, password: String) = withContext(Dispatchers.IO) {
        if (userName.isEmpty()) {
            _loginForm.postValue(LoginFormState(userNameError = R.string.user_name_must_not_be_empty))
        }
        else if (userName.length < 4 || userName.length > 10) {
            _loginForm.postValue(LoginFormState(userNameError = R.string.user_name_length))
        }else if (!isUserNameValid(userName)) {
            _loginForm.postValue(LoginFormState(userNameError = R.string.user_name_not_valid))
        } else if (checkIfUserExists(userName)==null) {
            _loginForm.postValue(LoginFormState(userNameError = R.string.user_does_not_exist))
        } else if (!isPasswordValid(password)) {
            _loginForm.postValue(LoginFormState(passwordError = R.string.password_length))
        }else if (verifyPassword(userName,password)==null) {
            _loginForm.postValue(LoginFormState(passwordError = R.string.password_does_not_match))
        }else {
            _loginForm.postValue(LoginFormState(isDataValid = true))
        }
    }


    /**
     * Username validation
     * */
    private fun isUserNameValid(username: String):Boolean{
        val pattern = Pattern.compile("^[a-zA-Z0-9]{4,10}$")
        val matcher: Matcher = pattern.matcher(username)
        return matcher.matches()
    }


    /** password validations */
    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    private suspend fun checkIfUserExists(username: String): User?{
        return authRepository.checkIfUserExists(username)
    }


    private suspend fun verifyPassword(username: String, password: String): User? {
        return authRepository.loginUser(username,password)
    }
}


