package com.example.storybaru.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storybaru.repositories.Repository
import kotlinx.coroutines.Dispatchers

class DetailViewModel(private val  repository: Repository):ViewModel() {

    fun getToken() = repository.getToken().asLiveData(Dispatchers.IO)

    fun getDetailStory(token : String,id:String) = repository.getDetailStories(token, id)


}