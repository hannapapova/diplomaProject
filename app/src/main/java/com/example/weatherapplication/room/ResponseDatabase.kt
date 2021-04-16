package com.example.weatherapplication.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapplication.model.*

@Database(
    entities = [Response::class,
        Alert::class,
        CurrentWeather::class,
        DailyWeather::class,
        FeelsLike::class,
        HourlyWeather::class,
        MinutelyWeather::class,
        Rain::class,
        Snow::class,
        Temperature::class,
        Weather::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ResponseDatabase : RoomDatabase() {

    abstract fun responseDao(): ResponseDao

    companion object {

        @Volatile
        private var INSTANCE: ResponseDatabase? = null

        fun getDatabase(context: Context): ResponseDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ResponseDatabase::class.java,
                    "counters_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}