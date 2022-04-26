package com.dicoding.picodiploma.storyapp1.data.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class RegisterResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class LoginResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("loginResult")
	val loginResult: LoginResult? = null
)

data class LoginResult(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("token")
	val token: String
)

data class StoryResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("listStory")
	val listStory: List<StoryItem>? = null,

	)

@Parcelize
data class StoryItem(

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("lat")
	val lat: Double? = null,

	@field:SerializedName("lon")
	val lon: Double? = null

) : Parcelable