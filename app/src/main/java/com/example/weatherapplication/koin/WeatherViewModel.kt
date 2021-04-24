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
import com.example.weatherapplication.room.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    val repository: ForecastRepository
    var currentWeather: LiveData<SavedCurrentWeather>
    var hourlyWeather: LiveData<List<SavedHourlyWeather>>
    var dailyWeather: LiveData<List<SavedDailyWeather>>
    var currentCity: LiveData<CurrentCity>
    var favouriteCities: LiveData<List<FavouriteCity>>
    var selectedCity = MutableLiveData<Geoname>()
    var suitableCities = MutableLiveData<List<Geoname>>()
    var latitude: Float = 53.893009F
    var longitude: Float = 27.567444F

    init {
//        Log.d("viewmodel", "init")
        val forecastDao = ForecastDatabase.getDatabase(application).forecastDao()
        repository = ForecastRepository(forecastDao)

//        getCitiesResult()
//
//        latitude = cities.value?.get(0)?.latitude?.toFloat() ?: 53.893009F
//        longitude = cities.value?.get(0)?.longitude?.toFloat() ?: 27.567444F
//
//        Log.d("viewmodel", "lat = $latitude, lon = $longitude")
//
//        getResult()

        currentWeather = repository.savedCurrentWeather
        hourlyWeather = repository.savedHourlyWeather
        dailyWeather = repository.savedDailyWeather
        currentCity = repository.savedCurrentCity
        favouriteCities = repository.favouriteCities
    }

    fun putSelectedCity(city: Geoname) {
        Log.d("viewmodel", "putSelectedCity ")
        Log.d("viewmodel", "putSelectedCity before = " + selectedCity.value)
        selectedCity.value = city
        Log.d("viewmodel", "putSelectedCity after = " + selectedCity.value)
    }

    fun putSelectedIntoFavourites(){
        val city = FavouriteCity(
            selectedCity.value!!.name,
            selectedCity.value!!.adminName1,
            selectedCity.value!!.countryName,
            selectedCity.value!!.latitude,
            selectedCity.value!!.longitude
        )
        insertFavouriteCity(city)
        favouriteCities = repository.favouriteCities
    }

    fun putSelectedIntoRepo() {
        Log.d("viewmodel", "putSelectedIntoRepo selectedCity: " + selectedCity.value)
        Log.d(
            "viewmodel",
            "putSelectedIntoRepo before delete: " + repository.savedCurrentCity.value
        )
        Log.d("viewmodel", "putSelectedIntoRepo before delete: " + currentCity.value)
        deleteCurrentCityTable()
        Log.d("viewmodel", "putSelectedIntoRepo after delete: " + repository.savedCurrentCity.value)
        Log.d("viewmodel", "putSelectedIntoRepo after delete: " + currentCity.value)
        val city = CurrentCity(
            selectedCity.value!!.name,
            selectedCity.value!!.adminName1,
            selectedCity.value!!.countryName,
            selectedCity.value!!.latitude,
            selectedCity.value!!.longitude
        )
//        Log.d("viewmodel", "putSelectedIntoRepo before insert: "+repository.savedCurrentCity.value)
//        Log.d("viewmodel", "putSelectedIntoRepo before insert: "+currentCity.value)
        insertCurrentCity(city)
        Log.d("viewmodel", "putSelectedIntoRepo after insert: " + repository.savedCurrentCity.value)
        Log.d("viewmodel", "putSelectedIntoRepo after insert: " + currentCity.value)
        currentCity = repository.savedCurrentCity
        Log.d("viewmodel", "putSelectedIntoRepo after =: " + repository.savedCurrentCity.value)
        Log.d("viewmodel", "putSelectedIntoRepo after =: " + currentCity.value)
    }



    fun newCoord(city: CurrentCity) {
//        Log.d("viewmodel", "in newCoord")

//            getCitiesResult()

//            Log.d("viewmodel", cities.value?.get(0).toString())

//            latitude = cities.value?.get(0)?.latitude?.toFloat() ?: 53.893009F
//            longitude = cities.value?.get(0)?.longitude?.toFloat() ?: 27.567444F

        latitude = city.latitude.toFloat()
        longitude = city.longitude.toFloat()

//        Log.d("viewmodel", "lat = $latitude, lon = $longitude")
    }

    fun getResult(city: CurrentCity) {
//        Log.d("viewmodel", "get result")
        newCoord(city)

        RetrofitInstance.weatherApi.getWeather(latitude, longitude, "metric")
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
//                        Log.d("viewmodel", "on response")
                        val responseBody: Response = response.body()!!
//                        Log.d("viewmodel", responseBody.toString())

                        //region Refresh forecast database
                        deleteCurrentWeatherTable()
                        deleteHourlyWeatherTable()
                        deleteDailyWeatherTable()

                        val newCurrentWeather = SavedCurrentWeather(
                            responseBody.timezone,
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

    fun getCitiesResult(cityName: String) {
//        Log.d("viewmodel", "getCitiesResult")

        RetrofitInstance.cityApi.getCities(cityName).enqueue(object : Callback<ResponseCity> {
            override fun onFailure(call: Call<ResponseCity>, t: Throwable) {
                Log.d("viewmodel", t.message.toString())
            }

            override fun onResponse(
                call: Call<ResponseCity>,
                response: retrofit2.Response<ResponseCity>
            ) {
                if (response.isSuccessful) {
//                    Log.d("viewmodel", "on response")
                    val responseBody: ResponseCity = response.body()!!

                    suitableCities.value = responseBody.geonames

//                    Log.d("viewmodel", "in getCitiesResult")
//                    Log.d("viewmodel", cities.value?.get(0).toString())

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

    fun insertCurrentCity(city: CurrentCity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCurrentCity(city)
        }
    }

    fun insertFavouriteCity(city: FavouriteCity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavouriteCity(city)
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

    fun deleteCurrentCityTable() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCurrentCityTable()
        }
    }
    //endregion
}