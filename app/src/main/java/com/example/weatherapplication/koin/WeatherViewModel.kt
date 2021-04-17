package com.example.weatherapplication.koin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.weatherapplication.api.RetrofitInstance
import com.example.weatherapplication.model.Response
import retrofit2.Call
import retrofit2.Callback

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    val weather = MutableLiveData<Response>()

    init {
        Log.d("viewmodel", "init")
        getResult()
    }

    fun getResult() {
        Log.d("viewmodel", "get result")
        RetrofitInstance.weatherApi.getWeather(53.893009F, 27.567444F, "metric").enqueue(object :
            Callback<Response> {
            // Network Errors here
            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.d("viewmodel", "on failure")
                Log.d("viewmodel", t.message.toString())
            }

            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    Log.d("viewmodel", "on response")
                    val responseBody = response.body()
                    Log.d("viewmodel", responseBody.toString())
                    weather.value = responseBody
                } else {
                    Log.d("viewmodel", "on response, but not successful")
                    Log.d("viewmodel", response.errorBody().toString())
                }
            }
        })
    }
}