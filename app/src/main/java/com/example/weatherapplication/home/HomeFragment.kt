package com.example.weatherapplication.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.weatherapplication.R
import com.example.weatherapplication.setupBar
import com.example.weatherapplication.setupNavigation

class HomeFragment : Fragment() {
    private val key = "HOME"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)

        setupBar(key, toolbar)
        setupNavigation(key, view, toolbar)
        return view
    }
}