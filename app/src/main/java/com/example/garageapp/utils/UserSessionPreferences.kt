package com.example.garageapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session_data_store")

data class UserLoginPreferences(val context: Context) {

    private val appContext = context.applicationContext

    val userId: Flow<Long?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[USER_ID]
        }

    val userName: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[USER_NAME]
        }

    val userPassword: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[USER_PASSWORD]
        }


    suspend fun saveUserId(userId: Long){
        appContext.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    suspend fun saveUserName(userName: String){
        appContext.dataStore.edit { preferences ->
            preferences[USER_NAME] = userName
        }
    }

    suspend fun saveUserPassword(userPassword : String){
        appContext.dataStore.edit { preferences ->
            preferences[USER_PASSWORD] = userPassword
        }
    }


    suspend fun clear() {
        appContext.dataStore.edit { preferences ->
            preferences.clear()
        }
    }


    companion object {
        private val USER_ID = longPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_PASSWORD = stringPreferencesKey("user_password")
    }

}