package com.example.weatherapplication.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapplication.room.dao.ForecastDao
import com.example.weatherapplication.room.entity.CurrentCity
import com.example.weatherapplication.room.entity.SavedCurrentWeather
import com.example.weatherapplication.room.entity.SavedDailyWeather
import com.example.weatherapplication.room.entity.SavedHourlyWeather

@Database(
    entities = [
        SavedCurrentWeather::class,
        SavedHourlyWeather::class,
        SavedDailyWeather::class,
        CurrentCity::class],
    version = 4,
    exportSchema = false
)
abstract class ForecastDatabase : RoomDatabase() {

    abstract fun forecastDao(): ForecastDao

    companion object {

        @Volatile
        private var INSTANCE: ForecastDatabase? = null

        fun getDatabase(context: Context): ForecastDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ForecastDatabase::class.java,
                    "forecast_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}