package com.dicoding.thestoryapp.api

import com.dicoding.thestoryapp.constant.TIMEOUT_REQUEST
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {

    fun getApiService(): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient
            .Builder()
            .connectTimeout(TIMEOUT_REQUEST, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_REQUEST, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_REQUEST, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java);
    }

}