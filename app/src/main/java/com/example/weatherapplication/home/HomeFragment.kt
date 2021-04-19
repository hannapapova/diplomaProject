package com.example.weatherapplication.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weatherapplication.*
import com.example.weatherapplication.adapter.DailyAdapter
import com.example.weatherapplication.adapter.HourlyAdapter
import com.example.weatherapplication.koin.WeatherViewModel
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

        setupObservers()
        setupRecyclers()

        refreshLayout.setOnRefreshListener {
            viewModel.getResult()
            refreshLayout.isRefreshing = false
        }
    }

    private fun setupObservers() {
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
        val statusPicture = requireActivity().findViewById<ImageView>(R.id.status_picture)
        val sunrise = requireActivity().findViewById<TextView>(R.id.sunrise)
        val sunset = requireActivity().findViewById<TextView>(R.id.sunset)
        val lastUpdateTime = requireActivity().findViewById<TextView>(R.id.last_update_date)

        val sunriseSunsetDateFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        val currentDateFormat = SimpleDateFormat("EEEE, dd MMMM", Locale.ENGLISH)

        viewModel.currentWeather.observe(viewLifecycleOwner, {
            title.text = it?.timezone ?: "wait"
            status.text =
                it?.weatherDescription?.get(0)?.toString()?.capitalize(Locale.ENGLISH) ?: "wait"
            temperature.text = it?.temperature.toString().plus("°C") ?: "wait"
            humidity.text = it?.humidity.toString().plus(" %") ?: "wait"
            windSpeed.text = it?.windSpeed.toString().plus(" km/h") ?: "wait"
            reelFeel.text = it?.feelsLike.toString().plus("°C") ?: "wait"
            dewPoint.text = "Dew Point: ".plus(it?.dewPoint?.toInt()).plus("°C") ?: "wait"
            uvIndex.text = "UV Index: ".plus(it?.uvIndex?.toInt()) ?: "wait"
            visibility.text = "Visibility: ".plus(it?.visibility?.div(1000)).plus(" km") ?: "wait"
            statusPicture.setImageResource(getStatusImage(it?.weatherMain))
            sunrise.text = if (it?.sunrise != null)
                sunriseSunsetDateFormat.format(Date(it.sunrise.toString().plus("000").toLong()))
            else "wait"

            sunset.text = if (it?.sunset != null)
                sunriseSunsetDateFormat.format(Date(it.sunset.toString().plus("000").toLong()))
            else "wait"

            lastUpdateTime.text = if (it?.dateTime != null)
                currentDateFormat.format(Date(it.dateTime.toString().plus("000").toLong()))
            else "wait"
        })

        viewModel.dailyWeather.observe(viewLifecycleOwner, {
            if (it?.size != 0) {
                rainChance.text =
                    it?.get(0)?.probabilityOfPrecipitation?.times(100).toString().plus(" %")
                precipitation.text =
                    "Precipitation: ".plus(((it[0].snow + it[0].rain) / 2)).plus(" mm")
            }
        })
    }

    private fun setupRecyclers() {
        val dailyRecycler = requireActivity().findViewById<RecyclerView>(R.id.recycler_daily)
        val hourlyRecycler = requireActivity().findViewById<RecyclerView>(R.id.recycler_hourly)

        viewModel.dailyWeather.observe(viewLifecycleOwner, {
            if (it?.size != 0)
                dailyRecycler.adapter = DailyAdapter(it)
        })

//        dailyRecycler.adapter = DailyAdapter(viewModel.dailyWeather.value)

        viewModel.hourlyWeather.observe(viewLifecycleOwner, {
            if (it?.size != 0)
                hourlyRecycler.adapter = HourlyAdapter(it)
        })

//        hourlyRecycler.adapter = HourlyAdapter(viewModel.hourlyWeather.value)
    }
}

internal fun getStatusImage(status: String?): Int {
    return when (status) {
        "Thunderstorm" -> R.drawable.severe_thunderstorm
        "Drizzle" -> R.drawable.drizzle
        "Rain" -> R.drawable.rain
        "Snow" -> R.drawable.snow
        "Mist", "Smoke", "Fog" -> R.drawable.fog
        "Haze", "Dust", "Sand", "Ash" -> R.drawable.dust
        "Squall", "Tornado" -> R.drawable.tornado
        "Clear" -> R.drawable.mostly_sunny
        "Clouds" -> R.drawable.mostly_cloudy
        else -> R.drawable.mostly_sunny
    }
}