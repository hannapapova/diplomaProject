package com.example.weatherapplication.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
}