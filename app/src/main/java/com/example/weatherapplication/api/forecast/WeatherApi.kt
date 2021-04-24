package com.example.weatherapplication.api.forecast

import com.example.weatherapplication.model.forecast.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("onecall?appid=f50a97432c54725678afdb6794cf9769")
    fun getWeather(
        @Query("lat")
        latitude: Float,
        @Query("lon")
        longitude: Float,
        @Query("units")
        units: String
    ) : Call<Response>
}