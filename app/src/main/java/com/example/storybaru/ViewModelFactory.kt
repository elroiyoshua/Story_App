package com.example.storybaru

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storybaru.di.Injection
import com.example.storybaru.feature.beranda.BerandaViewModel
import com.example.storybaru.feature.login.LoginViewModel
import com.example.storybaru.feature.register.RegisterViewModel
import com.example.storybaru.repositories.Repository

class ViewModelFactory private constructor(private val  repository:Repository):
    ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")

    override fun <T : ViewModel> create(modelClass: Class<T>):T{
      if(modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(repository) as T
        }else if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(BerandaViewModel::class.java)){
            return BerandaViewModel(repository) as T
      }
        throw java.lang.IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
   }


    companion object{
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory = instance?: synchronized(this) {
            instance ?: ViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}