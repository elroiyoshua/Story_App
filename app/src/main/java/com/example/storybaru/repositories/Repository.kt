package com.example.storybaru.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.storybaru.Data.AuthDataStore
import com.example.storybaru.Data.LocaleDataStore
import com.example.storybaru.api.ApiService
import com.example.storybaru.responses.LoginResponse
import com.example.storybaru.responses.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow



class Repository (
    private val apiService: ApiService,
    private val authDataStore: AuthDataStore,
    private val localeDataStore: LocaleDataStore
    ){
    fun getToken(): Flow<String?> = authDataStore.getToken()

    private suspend fun saveToken(token: String) {
        authDataStore.saveToken(token)
    }

    suspend fun clearToken() {
        authDataStore.clearToken()
    }

    fun getLocale(): Flow<String> = localeDataStore.getLocaleSetting()

    suspend fun saveLocale(locale: String) {
        localeDataStore.saveLocaleSetting(locale)
    }

    fun getUserLogin(email: String, password: String): LiveData<Result<LoginResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.login(email, password)
                saveToken(response.loginResult!!.token)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun saveUserRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object{
        @Volatile
        private var instance : Repository? = null

        fun getInstance(apiService: ApiService,authDataStore: AuthDataStore,localeDataStore: LocaleDataStore):Repository = instance ?: synchronized(this){
            instance ?:Repository(apiService,authDataStore,localeDataStore)
        }.also { instance = it }

    }
}