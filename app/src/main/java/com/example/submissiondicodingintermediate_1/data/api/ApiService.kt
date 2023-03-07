package com.example.submissiondicodingintermediate_1.data.api

import com.example.submissiondicodingintermediate_1.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("v1/register")
    fun createUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<SignUpResponse>

    @FormUrlEncoded
    @POST("v1/login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("v1/stories")
    suspend fun getAllStories(
        @Header("Authorization") bearer: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoriesResponse

    @Multipart
    @POST("v1/stories")
    fun uploadStories(
        @Header("Authorization") bearer: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<UploadStoriesResponse>

    @GET("v1/stories")
    fun storyLocation(
        @Header("Authorization") bearer: String,
        @Query("location") location: Int = 1
    ): Call<StoriesResponse>
}