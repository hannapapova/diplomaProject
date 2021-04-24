package com.example.weatherapplication

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.example.weatherapplication.koin.WeatherViewModel
import com.google.android.gms.location.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var myApplication: Application
    private var PERMISSION_ID = 322
    private val viewModel by inject<WeatherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)
        myApplication = application
        sharedPreferences = myApplication.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean("LOCATION", true)) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            getLastLocation()
        }

    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), PERMISSION_ID
        )
    }

    private fun isLocationServicesEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun getNewLocation() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            val location = p0.lastLocation
            viewModel.latitude = p0.lastLocation.latitude.toFloat()
            viewModel.longitude = p0.lastLocation.longitude.toFloat()
        }
    }


    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (isLocationServicesEnabled()) {
                fusedLocationClient.lastLocation.addOnCompleteListener {
                    val location = it.result
                    if (location == null) {
                        getNewLocation()
                    } else {
                        viewModel.latitude = it.result.latitude.toFloat()
                        viewModel.longitude = it.result.longitude.toFloat()
                    }
                }
            } else {
                Toast.makeText(this, "Please enable your location service", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("location", "Permission granted!")
            }
        }
    }
}

internal fun setupBar(key: String, toolbar: Toolbar?) {
    toolbar?.menu?.clear()
    when (key) {
        "HOME" -> {
            toolbar?.inflateMenu(R.menu.toolbar_home)
            toolbar?.setNavigationIcon(R.drawable.star_border_24)
        }
        "FAVORITES" -> {
            addBackButton(toolbar)
            toolbar?.menu?.findItem(R.id.action)?.isVisible = true
            toolbar?.menu?.findItem(R.id.action)?.setIcon(R.drawable.search_24)
        }
        "DETAILS" -> {
            addBackButton(toolbar)
        }
        "SEARCH" -> {
            addBackButton(toolbar)
        }
        "SETTINGS" -> {
            addBackButton(toolbar)
            toolbar?.menu?.findItem(R.id.action)?.isVisible = true
        }
    }
}

private fun addBackButton(toolbar: Toolbar?) {
    toolbar?.inflateMenu(R.menu.back_menu_with_search)
    toolbar?.setNavigationIcon(R.drawable.arrow_back_24)
}

internal fun setupBarActions(key: String, view: View, toolbar: Toolbar?) {
    when (key) {
        "HOME" -> {
            toolbar?.setNavigationOnClickListener {
                Navigation.findNavController(view).navigate(R.id.homeFragment_to_favoritesFragment)
            }
            toolbar?.setOnMenuItemClickListener {
                Navigation.findNavController(view)
                    .navigate(R.id.homeFragment_to_settingsFragment)
                true
            }
        }
        "FAVORITES" -> {
            toolbar?.setNavigationOnClickListener {
                Navigation.findNavController(view).popBackStack()
//                Navigation.findNavController(view)
//                    .navigate(R.id.action_favoritesFragment_to_homeFragment)
            }
            toolbar?.setOnMenuItemClickListener {
                Navigation.findNavController(view)
                    .navigate(R.id.favoritesFragment_to_searchFragment)
                true
            }
        }
        "DETAILS" -> {
            toolbar?.setNavigationOnClickListener {
                Navigation.findNavController(view).popBackStack()
            }
        }
        "SEARCH" -> {
            toolbar?.setNavigationOnClickListener {
                Navigation.findNavController(view).popBackStack()
            }
        }
        "SETTINGS" -> {
            toolbar?.setNavigationOnClickListener {
                Navigation.findNavController(view).popBackStack()
            }
        }
    }
}

internal fun setupTitle(title: TextView?, key: String) {
    when (key) {
        "HOME" -> {
            //TODO Set town name
            title?.text = "Home"
        }
        "FAVORITES" -> {
            title?.text = "Favorite"
        }
        "DETAILS" -> {
            title?.text = "Details"
        }
        "SEARCH" -> {
            title?.text = "Search"
        }
        "SETTINGS" -> {
            title?.text = "Settings"
        }
    }
}

internal fun setupStatusBarColor(window: Window?, context: Context) {
    //TODO Set color depending on weather status
    window?.statusBarColor = ContextCompat.getColor(context, R.color.end_color_sunny)
}

internal fun setupBackgroundColor(view: View) {
    //TODO Set background depending on weather status
    view.setBackgroundResource(R.drawable.sunny_gradient_bg)
}

internal fun setupToolBarBackgroundColor(toolbar: Toolbar?, context: Context) {
    //TODO Set color depending on weather status
    toolbar?.setBackgroundColor(ContextCompat.getColor(context, R.color.end_color_sunny))
}