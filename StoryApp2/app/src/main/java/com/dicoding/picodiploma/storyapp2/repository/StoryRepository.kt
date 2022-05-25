package com.dicoding.picodiploma.storyapp2.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.dicoding.picodiploma.storyapp2.data.database.StoryDatabase
import com.dicoding.picodiploma.storyapp2.data.network.ApiService
import com.dicoding.picodiploma.storyapp2.data.network.StoryItem
import com.dicoding.picodiploma.storyapp2.data.remotemediator.StoryRemoteMediator

class StoryRepository(private val token: String, private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getStory(): LiveData<PagingData<StoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(token, storyDatabase, apiService),
            pagingSourceFactory = {
//                QuotePagingSource(apiService)
                storyDatabase.storyDao().getAllStories()
            }
        ).liveData
    }
}