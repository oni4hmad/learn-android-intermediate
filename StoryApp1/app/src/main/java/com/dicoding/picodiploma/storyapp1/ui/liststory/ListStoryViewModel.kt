package com.dicoding.picodiploma.storyapp1.ui.liststory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.storyapp1.data.network.ApiConfig
import com.dicoding.picodiploma.storyapp1.data.network.StoryItem
import com.dicoding.picodiploma.storyapp1.data.network.StoryResponse
import com.dicoding.picodiploma.storyapp1.ui.Event
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListStoryViewModelFactory(private val token: String): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ListStoryViewModel(token) as T
}

class ListStoryViewModel(private val token: String) : ViewModel() {

    companion object {
        private const val TAG = "ListStoryViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listStory = MutableLiveData<List<StoryItem>>()
    val listStory: LiveData<List<StoryItem>> = _listStory

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    init {
        getStory(token, page = 1, size = 10)
    }

    fun getStory(token: String, page: Int, size: Int = 10) {
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
                            _listStory.value = it
                        }
                    }

                } else {
                    Log.e(TAG, "onFailure x: ${response.message()}")
                    response.errorBody()?.let {
                        val jObjError = JSONObject(it.string())
                        _toastText.value = Event(jObjError.getString("message"))
                    } ?: let {
                        _toastText.value = Event(response.message())
                    }
                }
            }
            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event(t.message.toString())
                Log.e(TAG, "onFailure y: ${t.message}")
            }
        })
    }
}