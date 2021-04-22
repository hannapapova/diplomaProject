package com.example.weatherapplication.favorites

import android.os.Bundle
import android.util.Log
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

        Log.d("viewmodel", "in fragment, getting cities")
        viewModel.getCitiesResult()

//        Log.d("viewmodel", "in fragment, getting result")
//        viewModel.getResult()

        viewModel.cities.observe(viewLifecycleOwner, Observer {
//            tv_hourly.text = it?.get(0).toString() ?: "wait"
            btn_city.text = it[0].name
        })

        viewModel.currentWeather.observe(viewLifecycleOwner, Observer {
            tv_current.text = it?.toString() ?: "wait"
        })

        btn_city.setOnClickListener {
            Log.d("viewmodel", "in fragment, getting coords")
            viewModel.newCoord()

            Log.d("viewmodel", "in fragment, getting result")
            viewModel.getResult()
        }

    }
}