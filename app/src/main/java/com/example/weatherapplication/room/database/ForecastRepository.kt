package com.example.weatherapplication.room.database

import com.example.weatherapplication.room.dao.ForecastDao
import com.example.weatherapplication.room.entity.*

class ForecastRepository(private val forecastDao: ForecastDao) {

    val savedCurrentWeather = forecastDao.loadCurrentWeather()

    val savedHourlyWeather = forecastDao.loadHourlyWeather()

    val savedDailyWeather = forecastDao.loadDailyWeather()

    val savedCurrentCity = forecastDao.loadCurrentCity()

    val favouriteCities = forecastDao.loadFavouriteCities()

    suspend fun insertCurrentWeather(currentWeather: SavedCurrentWeather) {
        forecastDao.insertCurrentWeather(currentWeather)
    }

    suspend fun insertHourlyWeatherList(hourlyWeather: List<SavedHourlyWeather>) {
        forecastDao.insertHourlyWeatherList(hourlyWeather)
    }

    suspend fun insertDailyWeatherList(dailyWeather: List<SavedDailyWeather>) {
        forecastDao.insertDailyWeatherList(dailyWeather)
    }

    suspend fun insertCurrentCity(currentCity: CurrentCity) {
        forecastDao.insertCurrentCity(currentCity)
    }

    suspend fun insertFavouriteCity(favouriteCity: FavouriteCity) {
        forecastDao.insertFavouriteCity(favouriteCity)
    }

    suspend fun deleteCurrentWeatherTable() {
        forecastDao.deleteCurrentWeatherTable()
    }

    suspend fun deleteHourlyWeatherTable() {
        forecastDao.deleteHourlyWeatherTable()
    }

    suspend fun deleteDailyWeatherTable() {
        forecastDao.deleteDailyWeatherTable()
    }

    suspend fun deleteCurrentCityTable() {
        forecastDao.deleteCurrentCityTable()
    }

    suspend fun deleteFavouriteCity(favouriteCity: FavouriteCity) {
        forecastDao.deleteFavouriteCity(favouriteCity)
    }
}