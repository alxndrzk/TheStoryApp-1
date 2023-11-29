package com.dicoding.thestoryapp.ui.story.viewmodel

import android.app.Application
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dicoding.thestoryapp.api.ApiConfig
import com.dicoding.thestoryapp.constant.PREF_TOKEN
import com.dicoding.thestoryapp.model.ResponseDetailStory
import com.dicoding.thestoryapp.model.ResponseGeneral
import com.dicoding.thestoryapp.model.ResponseListStory
import com.dicoding.thestoryapp.util.createPartFromStringData
import com.dicoding.thestoryapp.util.prepareFilePartData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

@Suppress("DEPRECATION")
class StoryViewModel(application: Application) : AndroidViewModel(application) {

    val storyListData: MutableLiveData<ResponseListStory> by lazy {
        MutableLiveData<ResponseListStory>()
    }

    val addStoryLiveData: MutableLiveData<ResponseGeneral> by lazy {
        MutableLiveData<ResponseGeneral>()
    }

    val detailStoryLiveData: MutableLiveData<ResponseDetailStory> by lazy {
        MutableLiveData<ResponseDetailStory>()
    }

    private val token = PreferenceManager.getDefaultSharedPreferences(application).getString(PREF_TOKEN, "")


    fun addStory(desc: String, file: File) {
        try {
            addStoryLiveData.value = null
            val bearer = "Bearer ${token as String}"
            val description = createPartFromStringData(desc)
            val storyFile = prepareFilePartData("photo", file)
            ApiConfig.getApiService().addStory(bearer, description, storyFile).enqueue(object : Callback<ResponseGeneral>{
                override fun onResponse(
                    call: Call<ResponseGeneral>,
                    response: Response<ResponseGeneral>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.error == false) {
                            addStoryLiveData.postValue(response.body())
                        }
                        else {
                            if (response.body()?.message != null && response.body()!!.message.isNotEmpty()) {
                                addStoryLiveData.postValue(ResponseGeneral(true, response.message()))
                            } else {
                                addStoryLiveData.postValue(ResponseGeneral(true, "error get data"))
                            }
                        }
                    } else if (response.code() == 401) {
                        addStoryLiveData.postValue(ResponseGeneral(true, "unauthorized"))
                    } else {
                        addStoryLiveData.postValue(ResponseGeneral(true, "error get data"))
                    }
                }

                override fun onFailure(call: Call<ResponseGeneral>, t: Throwable) {
                    t.printStackTrace()
                    addStoryLiveData.postValue(ResponseGeneral(true, "error get data"))
                }

            })
        }catch (e: Exception) {
            addStoryLiveData.postValue(ResponseGeneral(true, "error convert data"))
            e.printStackTrace()
        }
    }

    fun getAllStory() {
        try {
            storyListData.value = null
            val bearer = "Bearer ${token as String}"
            ApiConfig.getApiService().getListStory(bearer).enqueue(object : Callback<ResponseListStory>{
                override fun onResponse(
                    call: Call<ResponseListStory>,
                    response: Response<ResponseListStory>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.error == false) {
                            storyListData.postValue(response.body())
                        }
                        else {
                            if (response.body()?.message != null && response.body()!!.message.isNotEmpty()) {
                                storyListData.postValue(ResponseListStory(null, true, response.message()))
                            } else {
                                storyListData.postValue(ResponseListStory(null, true, "error get data"))
                            }
                        }
                    } else if (response.code() == 401) {
                        storyListData.postValue(ResponseListStory(null, true, "unauthorized"))
                    } else {
                        storyListData.postValue(ResponseListStory(null, true, "error get data"))
                    }
                }

                override fun onFailure(call: Call<ResponseListStory>, t: Throwable) {
                    t.printStackTrace()
                    storyListData.postValue(ResponseListStory(null, true, "error get data"))
                }

            })
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getDetailStory(id: String) {
        try {
            detailStoryLiveData.value = null
            val bearer = "Bearer ${token as String}"

            ApiConfig.getApiService().getDetailStory(bearer, id).enqueue(object: Callback<ResponseDetailStory>{
                override fun onResponse(
                    call: Call<ResponseDetailStory>,
                    response: Response<ResponseDetailStory>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.error == false) {
                            detailStoryLiveData.postValue(response.body())
                        }
                        else {
                            if (response.body()?.message != null && response.body()!!.message.isNotEmpty()) {
                                detailStoryLiveData.postValue(ResponseDetailStory(true, response.message(), null))
                            } else {
                                detailStoryLiveData.postValue(ResponseDetailStory(true, "error get data", null))
                            }
                        }
                    } else if (response.code() == 401) {
                        detailStoryLiveData.postValue(ResponseDetailStory(true, "unauthorized", null))
                    } else {
                        detailStoryLiveData.postValue(ResponseDetailStory(true, "error get data", null))
                    }
                }

                override fun onFailure(call: Call<ResponseDetailStory>, t: Throwable) {
                    detailStoryLiveData.postValue(ResponseDetailStory(true, "error get data", null))
                }

            })

        }catch (e: Exception) {
            e.printStackTrace()
            detailStoryLiveData.postValue(ResponseDetailStory(true, "error get data", null))
        }
    }

}