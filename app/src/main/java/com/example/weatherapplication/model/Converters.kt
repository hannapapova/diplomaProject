package com.example.weatherapplication.model

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types


class Converters {

    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun fromMinutelyWeatherList(minutelyWeatherList: List<MinutelyWeather>): String {
//        val moshi = Moshi.Builder().build()
        val listMyData = Types.newParameterizedType(
            MutableList::class.java,
            MinutelyWeather::class.java
        )
        val jsonAdapter: JsonAdapter<List<MinutelyWeather>> =
            moshi.adapter<List<MinutelyWeather>>(listMyData)
        return jsonAdapter.toJson(minutelyWeatherList)
    }

    @TypeConverter
    fun toMinutelyWeatherList(jsonString: String): List<MinutelyWeather>? {
//        val moshi = Moshi.Builder().build()
        val listMyData = Types.newParameterizedType(
            MutableList::class.java,
            MinutelyWeather::class.java
        )
        val jsonAdapter: JsonAdapter<List<MinutelyWeather>> =
            moshi.adapter<List<MinutelyWeather>>(listMyData)
        return jsonAdapter.fromJson(jsonString)
    }

    @TypeConverter
    fun fromHourlyWeatherList(hourlyWeatherList: List<HourlyWeather>): String {
//        val moshi = Moshi.Builder().build()
        val listMyData = Types.newParameterizedType(
            MutableList::class.java,
            HourlyWeather::class.java
        )
        val jsonAdapter: JsonAdapter<List<HourlyWeather>> =
            moshi.adapter<List<HourlyWeather>>(listMyData)
        return jsonAdapter.toJson(hourlyWeatherList)
    }

    @TypeConverter
    fun toHourlyWeatherList(jsonString: String): List<HourlyWeather>? {
//        val moshi = Moshi.Builder().build()
        val listMyData = Types.newParameterizedType(
            MutableList::class.java,
            HourlyWeather::class.java
        )
        val jsonAdapter: JsonAdapter<List<HourlyWeather>> =
            moshi.adapter<List<HourlyWeather>>(listMyData)
        return jsonAdapter.fromJson(jsonString)
    }

    @TypeConverter
    fun fromDailyWeatherList(dailyWeatherList: List<DailyWeather>): String {
//        val moshi = Moshi.Builder().build()
        val listMyData = Types.newParameterizedType(
            MutableList::class.java,
            DailyWeather::class.java
        )
        val jsonAdapter: JsonAdapter<List<DailyWeather>> =
            moshi.adapter<List<DailyWeather>>(listMyData)
        return jsonAdapter.toJson(dailyWeatherList)
    }

    @TypeConverter
    fun toDailyWeatherList(jsonString: String): List<DailyWeather>? {
//        val moshi = Moshi.Builder().build()
        val listMyData = Types.newParameterizedType(
            MutableList::class.java,
            DailyWeather::class.java
        )
        val jsonAdapter: JsonAdapter<List<DailyWeather>> =
            moshi.adapter<List<DailyWeather>>(listMyData)
        return jsonAdapter.fromJson(jsonString)
    }

    @TypeConverter
    fun fromAlertList(alertList: List<Alert>): String {
//        val moshi = Moshi.Builder().build()
        val listMyData = Types.newParameterizedType(
            MutableList::class.java,
            Alert::class.java
        )
        val jsonAdapter: JsonAdapter<List<Alert>> =
            moshi.adapter<List<Alert>>(listMyData)
        return jsonAdapter.toJson(alertList)
    }

    @TypeConverter
    fun toAlertList(jsonString: String): List<Alert>? {
//        val moshi = Moshi.Builder().build()
        val listMyData = Types.newParameterizedType(
            MutableList::class.java,
            Alert::class.java
        )
        val jsonAdapter: JsonAdapter<List<Alert>> =
            moshi.adapter<List<Alert>>(listMyData)
        return jsonAdapter.fromJson(jsonString)
    }

    @TypeConverter
    fun fromWeatherList(weatherList: List<Weather>): String {
//        val moshi = Moshi.Builder().build()
        val listMyData = Types.newParameterizedType(
            MutableList::class.java,
            Weather::class.java
        )
        val jsonAdapter: JsonAdapter<List<Weather>> =
            moshi.adapter<List<Weather>>(listMyData)
        return jsonAdapter.toJson(weatherList)
    }

    @TypeConverter
    fun toWeatherList(jsonString: String): List<Weather>? {
//        val moshi = Moshi.Builder().build()
        val listMyData = Types.newParameterizedType(
            MutableList::class.java,
            Weather::class.java
        )
        val jsonAdapter: JsonAdapter<List<Weather>> =
            moshi.adapter<List<Weather>>(listMyData)
        return jsonAdapter.fromJson(jsonString)
    }

    @TypeConverter
    fun fromCurrentWeather(currentWeather: CurrentWeather): String {
//        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<CurrentWeather> =
            moshi.adapter(CurrentWeather::class.java)
        return jsonAdapter.toJson(currentWeather)
    }

    @TypeConverter
    fun toCurrentWeather(jsonString: String): CurrentWeather? {
//        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<CurrentWeather> =
            moshi.adapter(CurrentWeather::class.java)
        return jsonAdapter.fromJson(jsonString)
    }

    @TypeConverter
    fun fromRain(rain: Rain): String {
//        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<Rain> =
            moshi.adapter(Rain::class.java)
        return jsonAdapter.toJson(rain)
    }

    @TypeConverter
    fun toRain(jsonString: String): Rain? {
//        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<Rain> =
            moshi.adapter(Rain::class.java)
        return jsonAdapter.fromJson(jsonString)
    }

    @TypeConverter
    fun fromSnow(snow: Snow): String {
//        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<Snow> =
            moshi.adapter(Snow::class.java)
        return jsonAdapter.toJson(snow)
    }

    @TypeConverter
    fun toSnow(jsonString: String): Snow? {
//        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<Snow> =
            moshi.adapter(Snow::class.java)
        return jsonAdapter.fromJson(jsonString)
    }

    @TypeConverter
    fun fromTemperature(temperature: Temperature): String {
//        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<Temperature> =
            moshi.adapter(Temperature::class.java)
        return jsonAdapter.toJson(temperature)
    }

    @TypeConverter
    fun toTemperature(jsonString: String): Temperature? {
//        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<Temperature> =
            moshi.adapter(Temperature::class.java)
        return jsonAdapter.fromJson(jsonString)
    }

    @TypeConverter
    fun fromFeelsLike(feelsLike: FeelsLike): String {
//        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<FeelsLike> =
            moshi.adapter(FeelsLike::class.java)
        return jsonAdapter.toJson(feelsLike)
    }

    @TypeConverter
    fun toFeelsLike(jsonString: String): FeelsLike? {
//        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<FeelsLike> =
            moshi.adapter(FeelsLike::class.java)
        return jsonAdapter.fromJson(jsonString)
    }
}