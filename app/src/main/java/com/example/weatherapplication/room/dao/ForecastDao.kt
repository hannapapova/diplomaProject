package com.example.weatherapplication.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapplication.room.entity.SavedCurrentWeather
import com.example.weatherapplication.room.entity.SavedDailyWeather
import com.example.weatherapplication.room.entity.SavedHourlyWeather

@Dao
interface ForecastDao {

    @Query("SELECT * FROM current_weather_table")
    fun loadCurrentWeather(): LiveData<SavedCurrentWeather>

    @Query("SELECT * FROM hourly_weather_table")
    fun loadHourlyWeather(): LiveData<List<SavedHourlyWeather>>

    @Query("SELECT * FROM daily_weather_table")
    fun loadDailyWeather(): LiveData<List<SavedDailyWeather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(currentWeather: SavedCurrentWeather)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyWeatherList(hourlyWeather: List<SavedHourlyWeather>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyWeatherList(dailyWeather: List<SavedDailyWeather>)

    @Query("DELETE FROM current_weather_table")
    suspend fun deleteCurrentWeatherTable()

    @Query("DELETE FROM hourly_weather_table")
    suspend fun deleteHourlyWeatherTable()

    @Query("DELETE FROM daily_weather_table")
    suspend fun deleteDailyWeatherTable()
}