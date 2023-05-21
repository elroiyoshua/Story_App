package com.example.storybaru.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.storybaru.Data.AuthDataStore
import com.example.storybaru.api.ApiService
import com.example.storybaru.responses.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


class Repository (
    private val apiService: ApiService,
    private val authDataStore: AuthDataStore,
    ){
    fun getToken(): Flow<String?> = authDataStore.getToken()

    private suspend fun saveToken(token: String) {
        authDataStore.saveToken(token)
    }

    suspend fun clearToken() {
        authDataStore.clearToken()
    }

    private fun generateBearerToken(token: String):String{
        return if (token.contains("bearer",true)){
            token
        }else{
            "Bearer $token"
        }
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

    fun saveUserRegister(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllStories(token: String):LiveData<com.example.storybaru.repositories.Result<AllStoriesResponses>> = liveData(Dispatchers.IO){
        emit(com.example.storybaru.repositories.Result.Loading)

        try{
            val response = apiService.getAllStories(generateBearerToken(token))
            emit(com.example.storybaru.repositories.Result.Success(response))
        }catch (e : Exception){
            emit(com.example.storybaru.repositories.Result.Error(e.message.toString()))
        }

    }
    fun getDetailStories(token: String,id : String):LiveData<com.example.storybaru.repositories.Result<DetailStoryResponses>> =
        liveData(Dispatchers.IO){
            emit(com.example.storybaru.repositories.Result.Loading)
            try {
                val response = apiService.getDetailStory(generateBearerToken(token),id)
                emit(com.example.storybaru.repositories.Result.Success(response))
            }catch (e:Exception){
                emit(com.example.storybaru.repositories.Result.Error(e.message.toString()))
            }
        }

    fun addStory(token: String,description : String,image : MultipartBody.Part) : LiveData<com.example.storybaru.repositories.Result<AddStoryResponses>> = liveData(Dispatchers.IO){
        emit(com.example.storybaru.repositories.Result.Loading)
        try {
            val response = apiService.addStory(generateBearerToken(token),description.toRequestBody("text/plain".toMediaType()),image)
            emit(com.example.storybaru.repositories.Result.Success(response))
        }catch (e : Exception){
            emit(com.example.storybaru.repositories.Result.Error(e.message.toString()))
        }
    }
    fun getLocation(token: String):LiveData<com.example.storybaru.repositories.Result<AllStoriesResponses>> = liveData(Dispatchers.IO){
        emit(com.example.storybaru.repositories.Result.Loading)
        try {
            val response = apiService.getLocation(generateBearerToken(token), location = 1)
            emit(com.example.storybaru.repositories.Result.Success(response))
        }catch (e:Exception){
            emit(com.example.storybaru.repositories.Result.Error(e.message.toString()))
        }
    }

    companion object{
        @Volatile
        private var instance : Repository? = null
        fun getInstance(apiService: ApiService,authDataStore: AuthDataStore):Repository = instance ?: synchronized(this){
            instance ?:Repository(apiService,authDataStore)
        }.also { instance = it }

    }
}