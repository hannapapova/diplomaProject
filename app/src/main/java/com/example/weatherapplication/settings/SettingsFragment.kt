package com.example.weatherapplication.settings

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.weatherapplication.*
import com.example.weatherapplication.setupBackgroundColor
import com.example.weatherapplication.setupBar
import com.example.weatherapplication.setupBarActions
import com.example.weatherapplication.setupTitle

class SettingsFragment : Fragment() {
    private val key = "SETTINGS"

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var myApplication: Application

    private lateinit var temperatureScales: RadioGroup
    private lateinit var windScales: RadioGroup
    private lateinit var pressureScales: RadioGroup
    private lateinit var visibilityScales: RadioGroup
    private lateinit var locationSettings: CheckBox

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        val title = activity?.findViewById<TextView>(R.id.fragment_name)

        setupTitle(title, key)
        setupBackgroundColor(view)
        setupBar(key, toolbar)
        setupBarActions(key, view, toolbar)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupLateInitValues()
        setupCheckedChangeListeners()
        loadSettings()
    }

    private fun loadSettings() {
        temperatureScales.check(sharedPreferences.getInt("TEMPERATURE_SCALE", R.id.celsius))
        windScales.check(sharedPreferences.getInt("WIND_SCALE", R.id.speed_km_h))
        pressureScales.check(sharedPreferences.getInt("PRESSURE_SCALE", R.id.atm_hPa))
        visibilityScales.check(sharedPreferences.getInt("VISIBILITY_SCALE", R.id.visibility_km))
        locationSettings.isChecked = sharedPreferences.getBoolean("LOCATION", true)
    }

    private fun setupCheckedChangeListeners() {
        editor = sharedPreferences.edit()
        temperatureScales.setOnCheckedChangeListener { _, checkedId ->
            editor.putInt("TEMPERATURE_SCALE", checkedId).apply()
        }

        windScales.setOnCheckedChangeListener { _, checkedId ->
            editor.putInt("WIND_SCALE", checkedId).apply()
        }

        pressureScales.setOnCheckedChangeListener { _, checkedId ->
            editor.putInt("PRESSURE_SCALE", checkedId).apply()
        }

        visibilityScales.setOnCheckedChangeListener { _, checkedId ->
            editor.putInt("VISIBILITY_SCALE", checkedId).apply()
        }

        locationSettings.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("LOCATION", isChecked).apply()
        }
    }

    private fun setupLateInitValues() {
        temperatureScales = requireActivity().findViewById(R.id.temperature_scales)
        windScales = requireActivity().findViewById(R.id.wind_scales)
        pressureScales = requireActivity().findViewById(R.id.pressure_scales)
        visibilityScales = requireActivity().findViewById(R.id.visibility_scales)
        locationSettings = requireActivity().findViewById(R.id.location_settings)
        myApplication = requireActivity().application
        sharedPreferences = myApplication.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE)
    }
}