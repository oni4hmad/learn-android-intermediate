package com.dicoding.picodiploma.storyapp2.ui.liststory

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.storyapp2.data.network.ApiConfig
import com.dicoding.picodiploma.storyapp2.data.network.StoryItem
import com.dicoding.picodiploma.storyapp2.data.network.StoryResponse
import com.dicoding.picodiploma.storyapp2.di.Injection
import com.dicoding.picodiploma.storyapp2.repository.StoryRepository
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListStoryViewModelFactory(private val token: String, private val context: Context): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListStoryViewModel(Injection.provideRepository(token, context)) as T
    }
}

class ListStoryViewModel(storyRepository: StoryRepository) : ViewModel() {

    val stories: LiveData<PagingData<StoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)

    /*private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listStory = MutableLiveData<List<StoryItem>>()
    val listStory: LiveData<List<StoryItem>> = _listStory

    private val _error = MutableLiveData<Error>()
    val error: LiveData<Error> = _error*/

    init {
        /*getStory(token, page = 1, size = 10)*/
    }

    /*fun getStory(token: String, page: Int = 1, size: Int = 10) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListStory(token, page, size)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { storyResponse ->
                        storyResponse.listStory?.let {
                            if (it.count() <= 0) {
                                _error.value = Error(true, type = ErrorType.NO_DATA)
                                return
                            }
                            _error.value = Error(false)
                            _listStory.value = it
                        }
                    }

                } else {
                    Log.e(TAG, "onFailure x: ${response.message()}")
                    response.errorBody()?.let {
                        val jObjError = JSONObject(it.string())
                        _error.value = Error(true, jObjError.getString("message"))
                    } ?: let {
                        _error.value = Error(true, response.message())
                    }
                }
            }
            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = Error(true, t.message.toString())
                Log.e(TAG, "onFailure y: ${t.message}")
            }
        })
    }*/

    /*inner class Error(
        val isError: Boolean,
        val errorMsg: String? = null,
        val type: ErrorType? = null
    )

    enum class ErrorType {
        NO_DATA
    }*/
}