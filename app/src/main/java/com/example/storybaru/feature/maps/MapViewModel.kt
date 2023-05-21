package com.example.storybaru.feature.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storybaru.repositories.Repository
import kotlinx.coroutines.Dispatchers

class MapViewModel(private val repository: Repository):ViewModel() {
    fun getToken() = repository.getToken().asLiveData(Dispatchers.IO)

    fun getLocation(token : String) = repository.getLocation(token)
}