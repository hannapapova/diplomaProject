package com.example.weatherapplication.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapplication.room.dao.ForecastDao
import com.example.weatherapplication.room.entity.*

@Database(
    entities = [
        SavedCurrentWeather::class,
        SavedHourlyWeather::class,
        SavedDailyWeather::class,
        CurrentCity::class,
        FavouriteCity::class],
    version = 9,
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