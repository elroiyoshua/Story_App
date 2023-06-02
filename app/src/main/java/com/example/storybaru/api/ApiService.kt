package com.example.storybaru.api

import com.example.storybaru.responses.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse


    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token :String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ):AllStoriesResponses

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Header("Authorization") token :String,
        @Path("id") id: String
    ):DetailStoryResponses

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part
    ):AddStoryResponses

    @GET("stories")
    suspend fun getLocation(
        @Header("Authorization") token :String,
        @Query("location") location : Int ?= null,
    ):AllStoriesResponses

}