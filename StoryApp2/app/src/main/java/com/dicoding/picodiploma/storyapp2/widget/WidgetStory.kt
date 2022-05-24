package com.dicoding.picodiploma.storyapp2.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dicoding.picodiploma.storyapp2.R
import com.dicoding.picodiploma.storyapp2.data.network.ApiConfig
import com.dicoding.picodiploma.storyapp2.data.network.StoryItem
import com.dicoding.picodiploma.storyapp2.data.network.StoryResponse
import com.dicoding.picodiploma.storyapp2.data.preferences.SessionPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


internal object WidgetStory {

    private var widgetImages = ArrayList<Bitmap>()
    private val getCount get() = widgetImages.count()
    val getImages get() = widgetImages

    private var maxImages = 5

    private fun removeAll() = widgetImages.clear()

    private fun addImage(img: Bitmap) {
        widgetImages.add(img)
    }

    fun getStory(context: Context, appWidgetId: Int, page: Int = 1, size: Int = maxImages) {

        SessionPreference(context).getAuthToken()?.also { token ->
            removeAll()
            val client = ApiConfig.getApiService().getListStory(token, page, size)
            client.enqueue(object : Callback<StoryResponse> {
                override fun onResponse(
                    call: Call<StoryResponse>,
                    response: Response<StoryResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { storyResponse ->
                            storyResponse.listStory?.let {
                                downloadImage(context, appWidgetId, it)
                            }
                        }
                    } else {
                        Log.e("WidgetStory.getStory", "onFailure x: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                    Log.e("WidgetStory.getStory", "onFailure y: ${t.message}")
                }
            })
        }

    }

    private fun downloadImage(context: Context, appWidgetId: Int, stories: List<StoryItem>) {
        stories.forEach {
            if (getCount <= maxImages) {
                Glide.with(context)
                    .asBitmap()
                    .load(it.photoUrl)
                    .apply(RequestOptions().override(400, 400))
                    .centerCrop()
                    .into(object : CustomTarget<Bitmap>(){
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            addImage(resource)
                            updateWidget(context, appWidgetId)
                        }
                        override fun onLoadCleared(placeholder: Drawable?) {
                            // this is called when imageView is cleared on lifecycle call or for some other reason.
                            // if you are referencing the bitmap somewhere else too other than this imageView
                            // clear it here as you can no longer have the bitmap
                        }
                    })
            } else return
        }
    }

    private fun updateWidget(context: Context, appWidgetId: Int) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stack_view)
    }

}