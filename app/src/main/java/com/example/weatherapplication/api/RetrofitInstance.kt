package com.example.weatherapplication.api

import com.example.weatherapplication.api.cities.CityApi
import com.example.weatherapplication.api.forecast.WeatherApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {

    private const val baseUrlForecast = "https://api.openweathermap.org/data/2.5/"
    private const val baseUrlCity = "https://api.geonames.org/"

    private val loggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofitForecast by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrlForecast)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private val retrofitCity by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrlCity)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val weatherApi: WeatherApi by lazy {
        retrofitForecast.create(
            WeatherApi::class.java
        )
    }

    val cityApi: CityApi by lazy {
        retrofitCity.create(
            CityApi::class.java
        )
    }
}