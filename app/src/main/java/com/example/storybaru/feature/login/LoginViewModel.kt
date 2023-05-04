package com.example.storybaru.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storybaru.repositories.Repository
import kotlinx.coroutines.Dispatchers

class LoginViewModel  (private val repository: Repository):ViewModel(){
    fun getUserLogin(email:String,password : String) = repository.getUserLogin(email, password)

    fun getToken() = repository.getToken().asLiveData(Dispatchers.IO)
}