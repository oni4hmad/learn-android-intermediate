package com.dicoding.picodiploma.storyapp1.data.network

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getListStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int = 10
    ): Call<StoryResponse>

}