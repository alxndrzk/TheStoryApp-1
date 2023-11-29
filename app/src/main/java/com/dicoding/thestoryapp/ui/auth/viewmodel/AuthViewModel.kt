package com.dicoding.thestoryapp.ui.auth.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.thestoryapp.api.ApiConfig
import com.dicoding.thestoryapp.model.ResponseLogin
import com.dicoding.thestoryapp.model.ResponseGeneral
import com.dicoding.thestoryapp.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class AuthViewModel: ViewModel() {

    val dataLogin: MutableLiveData<ResponseLogin> by lazy {
        MutableLiveData<ResponseLogin>()
    }

    val dataRegister: MutableLiveData<ResponseGeneral> by lazy {
        MutableLiveData<ResponseGeneral>()
    }

    fun login(email: String, password: String) {
        try {
            dataLogin.value = null
            val userLoginData = User(email = email, password = password)
            val service = ApiConfig.getApiService().login(userLoginData)
            service.enqueue(object : Callback<ResponseLogin>{
                override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                    if (response.isSuccessful) {
                        dataLogin.postValue(response.body())
                    }
                    else if (response.code() == 401) {
                        dataLogin.postValue(ResponseLogin(error = true, message = "Wrong email or password"))
                    }
                    else {
                        dataLogin.postValue(ResponseLogin(error = true, message = "error get data"))
                    }
                }

                override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                    t.printStackTrace()
                    dataLogin.postValue(ResponseLogin(error = true, message = "error get data"))
                }

            })
        }catch (e: Exception) {
            e.printStackTrace()
            dataLogin.postValue(ResponseLogin(error = true, message = "error get data"))

        }

    }

    fun register(name: String, email: String, password:String) {
        try {
            dataRegister.value = null
            val userRegisterData = User(name, email, password)
            val service = ApiConfig.getApiService().register(userRegisterData)
            service.enqueue(object : Callback<ResponseGeneral>{
                override fun onResponse(
                    call: Call<ResponseGeneral>,
                    response: Response<ResponseGeneral>
                ) {
                    if (response.isSuccessful) {
                        dataRegister.postValue(response.body())
                    }
                    else if (response.code() == 400) {
                        dataRegister.postValue(ResponseGeneral(error = true, "Email is already taken"))
                    }
                    else {
                        dataRegister.postValue(ResponseGeneral(error = true, "error get data"))

                    }
                }

                override fun onFailure(call: Call<ResponseGeneral>, t: Throwable) {
                    t.printStackTrace()
                    dataRegister.postValue(ResponseGeneral(error = true, "error get data"))

                }

            })
        }catch (e: Exception) {
            e.printStackTrace()
            dataRegister.postValue(ResponseGeneral(error = true, "error get data"))

        }
    }

}