package com.dicoding.picodiploma.storyapp2.data.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    suspend fun getListStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int = 10
    ): StoryResponse

    @GET("stories")
    fun getCallListStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int = 10
    ): Call<StoryResponse>

    @GET("stories")
    fun getMapListStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int = 10,
        @Query("location") location: Int = 1
    ): Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @PartMap params: HashMap<String, RequestBody>,
    ): Call<FileUploadResponse>

}