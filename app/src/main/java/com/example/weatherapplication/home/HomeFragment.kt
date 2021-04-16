package com.example.weatherapplication.home

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
import com.example.weatherapplication.setupBarActions
import com.example.weatherapplication.setupBar
import com.example.weatherapplication.setupTitle
import com.example.weatherapplication.koin.WeatherViewModel
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules

class HomeFragment : Fragment() {
    private val key = "HOME"
    private val viewModel by inject<WeatherViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("viewmodel", "on create view")
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        val title = activity?.findViewById<TextView>(R.id.fragment_name)
        val window = activity?.window

        setupTitle(title, key)
        setupToolBarBackgroundColor(toolbar, requireContext())
        setupBackgroundColor(view)
        setupStatusBarColor(window, requireContext())
        setupBar(key, toolbar)
        setupBarActions(key, view, toolbar)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("viewmodel", "on view created")

        viewModel.weather.observe(viewLifecycleOwner, Observer {
            Log.d("viewmodel", "observing")
        })
    }
}