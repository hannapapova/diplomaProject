package com.example.weatherapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.weatherapplication.viewmodel.WeatherViewModel

class HomeFragment : Fragment() {

    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textViewResponse = view.findViewById<TextView>(R.id.tv_response)

        weatherViewModel = activity?.run{
            ViewModelProviders.of(this)[WeatherViewModel::class.java]
        }!!

        weatherViewModel.weather.observe(viewLifecycleOwner, Observer {
            textViewResponse.text = it.toString()
        })
    }
}