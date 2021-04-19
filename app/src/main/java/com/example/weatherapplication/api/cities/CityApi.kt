package com.example.weatherapplication.api.cities

import com.example.weatherapplication.model.cities.ResponseCity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CityApi {
    @GET("searchJSON?username=hannapapova&style=medium&orderby=relevance")
    fun getCities(
        @Query("name_startsWith")
        query: String,
        @Query("lang")
        language: String = "ru"
    ): Call<ResponseCity>
}