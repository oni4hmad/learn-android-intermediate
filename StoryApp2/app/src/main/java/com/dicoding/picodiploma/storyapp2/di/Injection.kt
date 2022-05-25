package com.dicoding.picodiploma.storyapp2.di

import android.content.Context
import com.dicoding.picodiploma.storyapp2.data.database.StoryDatabase
import com.dicoding.picodiploma.storyapp2.data.network.ApiConfig
import com.dicoding.picodiploma.storyapp2.repository.StoryRepository

object Injection {
    fun provideRepository(token: String, context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(token, database, apiService)
    }
}