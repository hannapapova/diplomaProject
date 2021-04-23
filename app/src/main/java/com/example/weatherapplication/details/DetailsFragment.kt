package com.example.weatherapplication.details

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.weatherapplication.*
import com.example.weatherapplication.home.convertPressure
import com.example.weatherapplication.home.convertTemperature
import com.example.weatherapplication.home.convertWindSpeed
import com.example.weatherapplication.home.getStatusImage
import com.example.weatherapplication.setupBackgroundColor
import com.example.weatherapplication.setupBar
import com.example.weatherapplication.setupBarActions
import com.example.weatherapplication.setupTitle
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment : Fragment() {
    private val key = "DETAILS"
    private val args: DetailsFragmentArgs by navArgs()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var myApplication: Application

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        val title = activity?.findViewById<TextView>(R.id.fragment_name)

        setupTitle(title, key)
        setupBar(key, toolbar)
        setupBackgroundColor(view)
        setupBarActions(key, view, toolbar)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        myApplication = requireActivity().application
        sharedPreferences = myApplication.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE)
        val statusPicture = requireActivity().findViewById<ImageView>(R.id.status_picture_details)
        val status = requireActivity().findViewById<TextView>(R.id.status_details)
        val data = requireActivity().findViewById<TextView>(R.id.date_details)
        val dayTemperature = requireActivity().findViewById<TextView>(R.id.current_day_temp)
        val nightTemperature = requireActivity().findViewById<TextView>(R.id.current_night_temp)
        val sunrise = requireActivity().findViewById<TextView>(R.id.sunrise_field)
        val sunset = requireActivity().findViewById<TextView>(R.id.sunset_field)
        val feelsLikeDay = requireActivity().findViewById<TextView>(R.id.feelslike_day_field)
        val feelsLikeNight = requireActivity().findViewById<TextView>(R.id.feelslike_night_field)
        val pressure = requireActivity().findViewById<TextView>(R.id.pressure_field)
        val humidity = requireActivity().findViewById<TextView>(R.id.humidity)
        val dewPoint = requireActivity().findViewById<TextView>(R.id.dew_field)
        val uvIndex = requireActivity().findViewById<TextView>(R.id.uv_field)
        val windSpeed = requireActivity().findViewById<TextView>(R.id.wind_speed_field)
        val windDirection = requireActivity().findViewById<TextView>(R.id.wind_direction_field)
        val probabilityOfPrecipitation =
            requireActivity().findViewById<TextView>(R.id.precipitation_field)

        val mainDateFormatter = SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH)
        val sunriseSunsetFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)

        args.apply {
            statusPicture.setImageResource(getStatusImage(this.main))
            status.text = this.status.capitalize(Locale.ENGLISH)
            data.text = mainDateFormatter.format(Date(args.date.toString().plus("000").toLong()))
            dayTemperature.text = convertTemperature(this.tempDay, sharedPreferences.getInt("TEMPERATURE_SCALE", R.id.celsius))
            nightTemperature.text = convertTemperature(this.tempNight, sharedPreferences.getInt("TEMPERATURE_SCALE", R.id.celsius))
            sunrise.text =
                sunriseSunsetFormat.format(Date(args.sunrise.toString().plus("000").toLong()))
            sunset.text =
                sunriseSunsetFormat.format(Date(args.sunset.toString().plus("000").toLong()))
            feelsLikeDay.text = convertTemperature(this.feelslikeDay, sharedPreferences.getInt("TEMPERATURE_SCALE", R.id.celsius))
            feelsLikeNight.text = convertTemperature(this.feelslikeNight, sharedPreferences.getInt("TEMPERATURE_SCALE", R.id.celsius))
            pressure.text = convertPressure(
                this.pressure,
                sharedPreferences.getInt("PRESSURE_SCALE", R.id.atm_hPa)
            )
            humidity.text = this.humidity.toString().plus(" %")
            dewPoint.text = convertTemperature(
                this.dewPoint,
                sharedPreferences.getInt("TEMPERATURE_SCALE", R.id.celsius)
            )
            uvIndex.text = this.uvIndex.toInt().toString()
            windSpeed.text = convertWindSpeed(
                this.windSpeed,
                sharedPreferences.getInt("WIND_SCALE", R.id.speed_km_h)
            )
            windDirection.text = setupWindDirection(this.windDirection)
            probabilityOfPrecipitation.text =
                this.propabilityOfPrecipitation.times(100).toInt().toString().plus(" %")
        }
    }

    private fun setupWindDirection(windDirection: Int): String {
        return when (windDirection) {
            in 338..360, in 0..22 -> "North"
            in 293..337 -> "North - West"
            in 248..292 -> "West"
            in 203..247 -> "South - West"
            in 158..202 -> "South"
            in 113..157 -> "South - East"
            in 68..112 -> "East"
            in 23..67 -> "North - East"
            else -> "Null"
        }
    }
}