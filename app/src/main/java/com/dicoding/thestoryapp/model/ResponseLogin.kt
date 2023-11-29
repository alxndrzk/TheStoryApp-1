package com.dicoding.thestoryapp.model


import com.google.gson.annotations.SerializedName

data class ResponseLogin(
    @SerializedName("error")
    var error: Boolean,
    @SerializedName("loginResult")
    var loginResult: LoginResult? = null,
    @SerializedName("message")
    var message: String
)