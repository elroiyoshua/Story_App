package com.example.storybaru.feature.beranda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storybaru.repositories.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BerandaViewModel(private val repository: Repository):ViewModel() {

    fun clearToken(){
        viewModelScope.launch { repository.clearToken() }
    }

    fun getToken() = repository.getToken().asLiveData(Dispatchers.IO)


    fun getAllStories(token : String) = repository.getAllStories(token)

}