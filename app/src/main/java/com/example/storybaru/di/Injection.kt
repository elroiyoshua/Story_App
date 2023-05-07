package com.example.storybaru.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storybaru.Data.AuthDataStore
import com.example.storybaru.Data.LocaleDataStore
import com.example.storybaru.api.ApiConfig
import com.example.storybaru.repositories.Repository



val Context.dataStore: DataStore<Preferences> by preferencesDataStore("preferences")

object Injection {
    fun provideRepository(context: Context):Repository{
        val api = ApiConfig.getApiService()
        return Repository.getInstance(api, AuthDataStore.getInstance(context.dataStore), LocaleDataStore.getInstance(context.dataStore))

    }
}