package com.example.weatherapplication.api

import com.example.weatherapplication.model.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherApi {
    @GET("onecall?lat={latitude}&lon={longitude}&units={units}&appid=f50a97432c54725678afdb6794cf9769")
    fun getWeather(
        @Path("latitude")
        latitude: Float,
        @Path("longitude")
        longitude: Float,
        @Path("units")
        units: String
    ) : Call<Response>
}