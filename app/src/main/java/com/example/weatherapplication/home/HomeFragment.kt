package com.example.weatherapplication.home

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
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weatherapplication.*
import com.example.weatherapplication.adapter.DailyAdapter
import com.example.weatherapplication.adapter.HourlyAdapter
import com.example.weatherapplication.favorites.FavoritesFragmentDirections
import com.example.weatherapplication.koin.WeatherViewModel
import com.example.weatherapplication.model.cities.Geoname
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private val key = "HOME"
    private val viewModel by inject<WeatherViewModel>()
//    private val args: HomeFragmentArgs by navArgs()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var myApplication: Application

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        val title = requireActivity().findViewById<TextView>(R.id.fragment_name)
        val window = requireActivity().window

        viewModel.currentCity.value?.let { viewModel.getResult(it) }
        setupTitle(title, key)
        setupToolBarBackgroundColor(toolbar, requireContext())
        setupBackgroundColor(view)
        setupStatusBarColor(window, requireContext())
        setupBar(key, toolbar)
        setupBarActions(key, view, toolbar)

//        toolbar?.setNavigationOnClickListener {
//            val action = HomeFragmentDirections.homeFragmentToFavoritesFragment(
//                name = args.name,
//                adminName1 = args.adminName1,
//                countryName = args.countryName,
//                latitude = args.latitude,
//                longitude = args.longitude
//            )
//            Navigation.findNavController(view).navigate(action)
//        }
//        toolbar?.setOnMenuItemClickListener {
//            Navigation.findNavController(view)
//                .navigate(R.id.homeFragment_to_settingsFragment)
//            true
//        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        myApplication = requireActivity().application
        sharedPreferences = myApplication.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE)
        val refreshLayout =
            requireActivity().findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

        setupObservers()
        setupRecyclers()

        refreshLayout.setOnRefreshListener {
            viewModel.currentCity.value?.let { viewModel.getResult(it) }
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
            status.text = it?.weatherDescription?.capitalize(Locale.ENGLISH) ?: "wait"
            temperature.text = convertTemperature(
                it?.temperature,
                sharedPreferences.getInt("TEMPERATURE_SCALE", R.id.celsius)
            )
            humidity.text = it?.humidity.toString().plus(" %")
            windSpeed.text = convertWindSpeed(
                it?.windSpeed,
                sharedPreferences.getInt("WIND_SCALE", R.id.speed_km_h)
            )
            reelFeel.text = convertTemperature(
                it?.feelsLike,
                sharedPreferences.getInt("TEMPERATURE_SCALE", R.id.celsius)
            )
            dewPoint.text = "Dew Point: ".plus(
                convertTemperature(
                    it?.dewPoint,
                    sharedPreferences.getInt("TEMPERATURE_SCALE", R.id.celsius)
                )
            )
            uvIndex.text = "UV Index: ".plus(it?.uvIndex?.toInt())
            visibility.text = "Visibility: ".plus(
                convertLength(
                    it?.visibility,
                    sharedPreferences.getInt("VISIBILITY_SCALE", R.id.visibility_km)
                )
            )
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
                    it?.get(0)?.probabilityOfPrecipitation?.times(100)?.toInt().toString()
                        .plus(" %")
                precipitation.text =
                    "Precipitation: ".plus(it[0].rain).plus(" mm")
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

        viewModel.hourlyWeather.observe(viewLifecycleOwner, {
            if (it?.size != 0)
                hourlyRecycler.adapter = HourlyAdapter(it)
        })
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

internal fun convertLength(length: Int?, sharedPrefs: Int): String {
    return when (sharedPrefs) {
        R.id.visibility_km -> length?.div(1000).toString().plus(" km")
        R.id.visibility_ft -> length?.times(3.281)?.toInt().toString().plus(" ft")
        else -> "Error"
    }
}

internal fun convertWindSpeed(windSpeed: Float?, sharedPrefs: Int): String {
    return when (sharedPrefs) {
        R.id.speed_km_h -> windSpeed?.times(3.6)?.toInt().toString().plus(" km/h")
        R.id.speed_m_s -> windSpeed.toString().plus(" m/s")
        R.id.speed_mph -> windSpeed?.times(2.237)?.toInt().toString().plus(" mph")
        else -> "Error"
    }
}

internal fun convertTemperature(temperature: Float?, sharedPrefs: Int): String {
    return when (sharedPrefs) {
        R.id.celsius -> temperature?.toInt().toString().plus("°C")
        R.id.fahrenheit -> temperature?.times(1.8)?.toInt()?.plus(32).toString().plus("°F")
        R.id.kelvin -> temperature?.plus(273.15)?.toInt().toString().plus("K")
        else -> "Error"
    }
}

internal fun convertPressure(pressure: Int, sharedPrefs: Int): String {
    return when (sharedPrefs) {
        R.id.atm_hPa -> pressure.toString().plus(" hPa")
        R.id.atm_mmHg -> pressure.div(1.333).toInt().toString().plus(" mmHg")
        else -> "Error"
    }
}