package com.example.storybaru.feature.addstory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storybaru.repositories.Repository
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody

class AddStoryViewModel(private val repository: Repository ) : ViewModel() {

    fun getToken()  = repository.getToken().asLiveData(Dispatchers.IO)

    fun addStory(token : String,description : String,image : MultipartBody.Part) = repository.addStory(token,description, image)
}