package com.example.weatherapplication.koin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.api.RetrofitInstance
import com.example.weatherapplication.model.cities.Geoname
import com.example.weatherapplication.model.cities.ResponseCity
import com.example.weatherapplication.model.forecast.Response
import com.example.weatherapplication.room.database.ForecastDatabase
import com.example.weatherapplication.room.database.ForecastRepository
import com.example.weatherapplication.room.entity.SavedCurrentWeather
import com.example.weatherapplication.room.entity.SavedDailyWeather
import com.example.weatherapplication.room.entity.SavedHourlyWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ForecastRepository
    lateinit var currentWeather: LiveData<SavedCurrentWeather>
    lateinit var hourlyWeather: LiveData<List<SavedHourlyWeather>>
    lateinit var dailyWeather: LiveData<List<SavedDailyWeather>>
    var cities = MutableLiveData<List<Geoname>>()
    private lateinit var latitude: String
    private lateinit var longitude: String

    init {
        Log.d("viewmodel", "init")
        val forecastDao = ForecastDatabase.getDatabase(application).forecastDao()
        repository = ForecastRepository(forecastDao)

        getCitiesResult()

        latitude = cities.value!![0].latitude
        longitude = cities.value!![0].longitude

//        getResult()
//
//        currentWeather = repository.savedCurrentWeather
//        hourlyWeather = repository.savedHourlyWeather
//        dailyWeather = repository.savedDailyWeather
    }

    fun getResult() {
        Log.d("viewmodel", "get result")
        RetrofitInstance.weatherApi.getWeather(latitude.toFloat(), longitude.toFloat(), "metric")
            .enqueue(object :
                Callback<Response> {
                // Network Errors here
                override fun onFailure(call: Call<Response>, t: Throwable) {
                    Log.d("viewmodel", "on failure")
                    Log.d("viewmodel", t.message.toString())
                }

                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    if (response.isSuccessful) {
                        Log.d("viewmodel", "on response")
                        val responseBody: Response = response.body()!!
                        Log.d("viewmodel", responseBody.toString())

                        //region Refresh forecast database
                        deleteCurrentWeatherTable()
                        deleteHourlyWeatherTable()
                        deleteDailyWeatherTable()

                        val newCurrentWeather = SavedCurrentWeather(
                            responseBody.current.dateTime,
                            responseBody.current.sunrise,
                            responseBody.current.sunset,
                            responseBody.current.temperature,
                            responseBody.current.feelsLike,
                            responseBody.current.pressure,
                            responseBody.current.humidity,
                            responseBody.current.dewPoint,
                            responseBody.current.clouds,
                            responseBody.current.uvIndex,
                            responseBody.current.visibility,
                            responseBody.current.windSpeed,
                            responseBody.current.windDirection,
                            responseBody.current.rain?.rainVolume ?: 0F,
                            responseBody.current.snow?.snowVolume ?: 0F,
                            responseBody.current.weather[0].main,
                            responseBody.current.weather[0].description
                        )
                        insertCurrentWeather(newCurrentWeather)

                        val newHourlyWeatherList = mutableListOf<SavedHourlyWeather>()
                        for (hourly in responseBody.hourly) {
                            newHourlyWeatherList.add(
                                SavedHourlyWeather(
                                    hourly.dateTime,
                                    hourly.temperature,
                                    hourly.weather[0].main,
                                    hourly.weather[0].description
                                )
                            )
                        }
                        insertHourlyWeatherList(newHourlyWeatherList)

                        val newDailyWeatherList = mutableListOf<SavedDailyWeather>()
                        for (daily in responseBody.daily) {
                            newDailyWeatherList.add(
                                SavedDailyWeather(
                                    daily.dateTime,
                                    daily.sunrise,
                                    daily.sunset,
                                    daily.temperature.day,
                                    daily.temperature.night,
                                    daily.temperature.morning,
                                    daily.temperature.evening,
                                    daily.feelsLike.day,
                                    daily.feelsLike.night,
                                    daily.feelsLike.morning,
                                    daily.feelsLike.evening,
                                    daily.pressure,
                                    daily.humidity,
                                    daily.dewPoint,
                                    daily.uvIndex,
                                    daily.clouds,
                                    daily.windSpeed,
                                    daily.windDirection,
                                    daily.probabilityOfPrecipitation,
                                    daily.rain,
                                    daily.snow,
                                    daily.weather[0].main,
                                    daily.weather[0].description
                                )
                            )
                        }
                        insertDailyWeatherList(newDailyWeatherList)
                        //endregion

                        currentWeather = repository.savedCurrentWeather
                        hourlyWeather = repository.savedHourlyWeather
                        dailyWeather = repository.savedDailyWeather

                    } else {
                        Log.d("viewmodel", "on response, but not successful")
                        Log.d("viewmodel", response.errorBody().toString())
                    }
                }
            })
    }

    fun getCitiesResult() {
        RetrofitInstance.cityApi.getCities("фаниполь").enqueue(object : Callback<ResponseCity> {
            override fun onFailure(call: Call<ResponseCity>, t: Throwable) {
                Log.d("viewmodel", t.message.toString())
            }

            override fun onResponse(
                call: Call<ResponseCity>,
                response: retrofit2.Response<ResponseCity>
            ) {
                if (response.isSuccessful) {
                    Log.d("viewmodel", "on response")
                    val responseBody: ResponseCity = response.body()!!
                    Log.d("viewmodel", responseBody.toString())

                    cities.value = responseBody.geonames

                    Log.d("viewmodel", "__________________")
                    Log.d("viewmodel", cities.value.toString())

                } else {
                    Log.d("viewmodel", "on response, but not successful")
                    Log.d("viewmodel", response.errorBody().toString())
                }
            }

        })
    }

    //region Room functions for forecast
    fun insertCurrentWeather(currentWeather: SavedCurrentWeather) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCurrentWeather(currentWeather)
        }
    }

    fun insertHourlyWeatherList(hourlyWeather: List<SavedHourlyWeather>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertHourlyWeatherList(hourlyWeather)
        }
    }

    fun insertDailyWeatherList(dailyWeather: List<SavedDailyWeather>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertDailyWeatherList(dailyWeather)
        }
    }

    fun deleteCurrentWeatherTable() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCurrentWeatherTable()
        }
    }

    fun deleteHourlyWeatherTable() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteHourlyWeatherTable()
        }
    }

    fun deleteDailyWeatherTable() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDailyWeatherTable()
        }
    }
    //endregion
}