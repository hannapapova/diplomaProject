package com.example.weatherapplication.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weatherapplication.*
import com.example.weatherapplication.koin.WeatherViewModel
import com.example.weatherapplication.setupBarActions
import com.example.weatherapplication.setupBar
import com.example.weatherapplication.setupTitle
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private val key = "HOME"
    private val viewModel by inject<WeatherViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        val title = requireActivity().findViewById<TextView>(R.id.fragment_name)
        val window = requireActivity().window

        viewModel.getResult()
        setupTitle(title, key)
        setupToolBarBackgroundColor(toolbar, requireContext())
        setupBackgroundColor(view)
        setupStatusBarColor(window, requireContext())
        setupBar(key, toolbar)
        setupBarActions(key, view, toolbar)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val refreshLayout =
            requireActivity().findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

        refreshLayout.setOnRefreshListener {
            viewModel.getResult()
            setupCityInfo()
            refreshLayout.isRefreshing = false
        }
    }

    private fun setupCityInfo() {
        val title = requireActivity().findViewById<TextView>(R.id.fragment_name)
        val status = requireActivity().findViewById<TextView>(R.id.status)
        val temperature = requireActivity().findViewById<TextView>(R.id.temperature)
        val humidity = requireActivity().findViewById<TextView>(R.id.humidity)
        val windSpeed = requireActivity().findViewById<TextView>(R.id.wind_speed)
        val reelFeel = requireActivity().findViewById<TextView>(R.id.real_feel)
        val rainChance = requireActivity().findViewById<TextView>(R.id.rain_chance)
        val dewPoint = requireActivity().findViewById<TextView>(R.id.dew_point)
        val precipitation = requireActivity().findViewById<TextView>(R.id.precipitation)
        val uvIndex = requireActivity().findViewById<TextView>(R.id.uv_index)
        val visibility = requireActivity().findViewById<TextView>(R.id.visibility)

        timeSetup()
        setStatusPicture(viewModel.weather.value?.current?.weather?.get(0)?.main.toString())

        title.text = viewModel.weather.value?.timezone.toString()
        status.text =
            viewModel.weather.value?.current?.weather?.get(0)?.description.toString().capitalize(
                Locale.ENGLISH
            )
        temperature.text =
            viewModel.weather.value?.current?.temperature?.toInt().toString().plus("°C")
        humidity.text = viewModel.weather.value?.current?.humidity?.toInt().toString().plus(" %")
        windSpeed.text = viewModel.weather.value?.current?.windSpeed.toString().plus(" km/h")
        reelFeel.text = viewModel.weather.value?.current?.feelsLike?.toInt().toString().plus("°C")
        rainChance.text =
            viewModel.weather.value?.daily?.get(0)?.probabilityOfPrecipitation.toString()
        dewPoint.text =
            "Dew Point: ".plus(viewModel.weather.value?.current?.dewPoint?.toInt().toString())
                .plus("°C")
        uvIndex.text =
            "UV Index: ".plus(viewModel.weather.value?.current?.uvIndex?.toInt().toString())
        visibility.text =
            "Visibility: ".plus(viewModel.weather.value?.current?.visibility?.div(1000)).plus(" km")
        precipitation.text =
            "Precipitation: ".plus(viewModel.weather.value?.daily?.get(0)?.rain?.toString())
                .plus(" mm")
    }

    private fun setStatusPicture(status: String) {
        val statusPicture = requireActivity().findViewById<ImageView>(R.id.status_picture)
        when (status) {
            "Thunderstorm" -> statusPicture.setImageResource(R.drawable.severe_thunderstorm)
            "Drizzle" -> statusPicture.setImageResource(R.drawable.drizzle)
            "Rain" -> statusPicture.setImageResource(R.drawable.rain)
            "Snow" -> statusPicture.setImageResource(R.drawable.snow)
            "Mist", "Smoke",  "Fog" -> statusPicture.setImageResource(R.drawable.fog)
            "Haze", "Dust", "Sand", "Ash" -> statusPicture.setImageResource(R.drawable.dust)
            "Squall", "Tornado" -> statusPicture.setImageResource(R.drawable.tornado)
            "Clear" -> statusPicture.setImageResource(R.drawable.mostly_sunny)
            "Clouds" -> statusPicture.setImageResource(R.drawable.mostly_cloudy)
        }
    }

    private fun timeSetup() {
        val sunrise = requireActivity().findViewById<TextView>(R.id.sunrise)
        val sunset = requireActivity().findViewById<TextView>(R.id.sunset)
        val lastUpdateTime = requireActivity().findViewById<TextView>(R.id.last_update_date)
        val sunriseSunsetDateFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        val currentDateFormat = SimpleDateFormat("EEEE, dd MMMM", Locale.ENGLISH)
        val currentDate =
            Date(viewModel.weather.value?.current?.dateTime.toString().plus("000").toLong())
        val sunriseDate =
            Date(viewModel.weather.value?.current?.sunrise.toString().plus("000").toLong())
        val sunsetDate =
            Date(viewModel.weather.value?.current?.sunset.toString().plus("000").toLong())

        sunrise.text = sunriseSunsetDateFormat.format(sunriseDate)
        sunset.text = sunriseSunsetDateFormat.format(sunsetDate)
        lastUpdateTime.text = currentDateFormat.format(currentDate)
    }
}