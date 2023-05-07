package com.example.storybaru.feature.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storybaru.repositories.Repository
import kotlinx.coroutines.Dispatchers

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    fun saveUserRegister(name: String, email: String, password: String) = repository.saveUserRegister(name, email, password)

}