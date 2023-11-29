package com.dicoding.thestoryapp.api

import com.dicoding.thestoryapp.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @Headers("Content-Type: application/json", "X-Requested-With: XMLHttpRequest")
    @POST("login")
    fun login(@Body user: User): Call<ResponseLogin>

    @POST("register")
    fun register(@Body user: User): Call<ResponseGeneral>

    @GET("stories")
    fun getListStory(@Header("Authorization") authorization: String): Call<ResponseListStory>

    @GET("stories")
    fun getListStory(@Header("Authorization") authorization: String, @Query("location") location: Int): Call<ResponseListStory>

    @GET("stories/{id}")
    fun getDetailStory(@Header("Authorization") authorization: String, @Path("id") id: String): Call<ResponseDetailStory>

    @Multipart
    @POST("stories")
    fun addStory(@Header("Authorization") authorization: String, @Part("description") description: RequestBody, @Part file: MultipartBody.Part): Call<ResponseGeneral>

}