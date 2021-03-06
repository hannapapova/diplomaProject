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
    var suitableCities = MutableLiveData<MutableList<Geoname>>()
    var cityGps = MutableLiveData<CurrentCity>()
    var latitude: Float = 0F
    var longitude: Float = 0F

    init {
        val forecastDao = ForecastDatabase.getDatabase(application).forecastDao()
        repository = ForecastRepository(forecastDao)
        currentWeather = repository.savedCurrentWeather()
        hourlyWeather = repository.savedHourlyWeather()
        dailyWeather = repository.savedDailyWeather()
        currentCity = repository.savedCurrentCity
        favouriteCities = repository.favouriteCities
    }

    fun setSelectedCity(city: Geoname) {
        selectedCity.value = city
    }

    fun setGPSAsSelected() {
        selectedCity.value = Geoname(
            cityGps.value?.adminName1!!,
            cityGps.value?.countryName!!,
            cityGps.value?.latitude!!,
            cityGps.value?.longitude!!,
            cityGps.value?.name!!
        )
    }

    fun setCurrentAsSelected() {
        selectedCity.value = Geoname(
            currentCity.value?.adminName1!!,
            currentCity.value?.countryName!!,
            currentCity.value?.latitude!!,
            currentCity.value?.longitude!!,
            currentCity.value?.name!!
        )
    }

    fun setSelectedAsCurrent() {
        deleteCurrentCityTable()
        val city = CurrentCity(
            selectedCity.value!!.name,
            selectedCity.value!!.adminName1,
            selectedCity.value!!.countryName,
            selectedCity.value!!.latitude,
            selectedCity.value!!.longitude
        )
        insertCurrentCity(city)
        currentCity = repository.savedCurrentCity
    }

    fun setSelectedAsGPS() {
        cityGps.value = geoNameToCurrentCity(selectedCity.value!!)
    }

    fun putSelectedIntoFavouritesDB(geoName: Geoname = selectedCity.value!!) {
        val city = FavouriteCity(
            geoName.name,
            geoName.adminName1,
            geoName.countryName,
            geoName.latitude,
            geoName.longitude
        )
        insertFavouriteCity(city)
        favouriteCities = repository.favouriteCities
    }

    fun deleteNotFavouritesFromDB(cities: List<FavouriteCity>) {
        for (city in cities) {
            if (!city.inFavourites)
                deleteFavouriteCity(city)
        }
        favouriteCities = repository.favouriteCities
    }

    fun getForecast(city: CurrentCity) {
        getCoordinatesOfCity(city)
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
                        val responseBody: Response = response.body()!!

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

                        viewModelScope.launch(Dispatchers.IO) {
                            currentWeather = repository.savedCurrentWeather()
                            hourlyWeather = repository.savedHourlyWeather()
                            dailyWeather = repository.savedDailyWeather()
                        }
                    } else {
                        Log.d("viewmodel", "on response, but not successful")
                        Log.d("viewmodel", response.errorBody().toString())
                    }
                }
            })
    }

    fun getCitiesResult(cityName: String) {
        RetrofitInstance.cityApi.getCities(cityName).enqueue(object : Callback<ResponseCity> {
            override fun onFailure(call: Call<ResponseCity>, t: Throwable) {
                Log.d("viewmodel", t.message.toString())
            }

            override fun onResponse(
                call: Call<ResponseCity>,
                response: retrofit2.Response<ResponseCity>
            ) {
                if (response.isSuccessful) {
                    val responseBody: ResponseCity = response.body()!!
                    val list = mutableListOf<Geoname>()
                    for (location in responseBody.geonames) {
                        if (location.name != "" &&
                            location.adminName1 != "" &&
                            location.countryName != "" &&
                            location.latitude != "" &&
                            location.longitude != ""
                        ) {
                            list.add(location)
                        }
                    }
                    suitableCities.value = list
                } else {
                    Log.d("viewmodel", "on response, but not successful")
                    Log.d("viewmodel", response.errorBody().toString())
                }
            }
        })
    }

    private fun getCoordinatesOfCity(city: CurrentCity) {
        latitude = city.latitude.toFloat()
        longitude = city.longitude.toFloat()
    }

    private fun geoNameToCurrentCity(geoName: Geoname): CurrentCity {
        return CurrentCity(
            geoName.name,
            geoName.adminName1,
            geoName.countryName,
            geoName.latitude,
            geoName.longitude
        )
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

    private fun insertCurrentCity(city: CurrentCity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCurrentCity(city)
        }
    }

    private fun insertFavouriteCity(city: FavouriteCity) {
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

    private fun deleteCurrentCityTable() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCurrentCityTable()
        }
    }

    fun deleteFavouriteCity(favouriteCity: FavouriteCity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavouriteCity(favouriteCity)
        }
    }
    //endregion
}