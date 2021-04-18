package com.example.weatherapplication.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.weatherapplication.*
import com.example.weatherapplication.koin.WeatherViewModel
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.koin.android.ext.android.inject

class FavoritesFragment : Fragment() {
    private val key = "FAVORITES"
    private val viewModel by inject<WeatherViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        val title = activity?.findViewById<TextView>(R.id.fragment_name)

        setupTitle(title, key)
        setupBackgroundColor(view)
        setupBar(key, toolbar)
        setupBarActions(key, view, toolbar)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentWeather.observe(viewLifecycleOwner, Observer {
            tv_current.text = it?.toString() ?: "wait"
        })

        viewModel.hourlyWeather.observe(viewLifecycleOwner, Observer {
            tv_hourly.text = it?.toString() ?: "wait"
        })

        viewModel.dailyWeather.observe(viewLifecycleOwner, Observer {
            tv_daily.text = it?.toString() ?: "wait"
        })
    }
}