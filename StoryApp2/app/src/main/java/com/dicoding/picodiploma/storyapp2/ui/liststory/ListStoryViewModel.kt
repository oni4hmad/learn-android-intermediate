package com.dicoding.picodiploma.storyapp2.ui.liststory

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.storyapp2.data.network.StoryItem
import com.dicoding.picodiploma.storyapp2.di.Injection
import com.dicoding.picodiploma.storyapp2.repository.StoryRepository

class ListStoryViewModelFactory(private val token: String, private val context: Context): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListStoryViewModel(Injection.provideRepository(token, context)) as T
    }
}

class ListStoryViewModel(storyRepository: StoryRepository) : ViewModel() {

    val stories: LiveData<PagingData<StoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)
    
}