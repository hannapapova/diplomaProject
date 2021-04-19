package com.example.weatherapplication.room.database

import com.example.weatherapplication.room.dao.ForecastDao
import com.example.weatherapplication.room.entity.SavedCurrentWeather
import com.example.weatherapplication.room.entity.SavedDailyWeather
import com.example.weatherapplication.room.entity.SavedHourlyWeather

class ForecastRepository(private val forecastDao: ForecastDao) {

    val savedCurrentWeather = forecastDao.loadCurrentWeather()

    val savedHourlyWeather = forecastDao.loadHourlyWeather()

    val savedDailyWeather = forecastDao.loadDailyWeather()

    suspend fun insertCurrentWeather(currentWeather: SavedCurrentWeather) {
        forecastDao.insertCurrentWeather(currentWeather)
    }

    suspend fun insertHourlyWeatherList(hourlyWeather: List<SavedHourlyWeather>) {
        forecastDao.insertHourlyWeatherList(hourlyWeather)
    }

    suspend fun insertDailyWeatherList(dailyWeather: List<SavedDailyWeather>) {
        forecastDao.insertDailyWeatherList(dailyWeather)
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
}