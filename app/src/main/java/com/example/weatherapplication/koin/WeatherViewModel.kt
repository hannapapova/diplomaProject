package com.example.weatherapplication.koin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapplication.api.RetrofitInstance
import com.example.weatherapplication.model.Response
import retrofit2.Call
import retrofit2.Callback

class WeatherViewModel : ViewModel() {

    val weather = MutableLiveData<Response>()

    fun getResult() {
        RetrofitInstance.weatherApi.getWeather(53.893009F, 27.567444F, "metric").enqueue(object :
            Callback<Response> {
            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.e("WeatherViewModel", t.message.toString())
            }

            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    weather.value = responseBody
                } else {
                    Log.e("WeatherViewModel", response.errorBody().toString())
                }
            }
        })
    }
}