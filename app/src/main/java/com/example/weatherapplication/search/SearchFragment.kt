package com.example.weatherapplication.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.weatherapplication.R
import com.example.weatherapplication.setupBar
import com.example.weatherapplication.setupActionsBar

class SearchFragment : Fragment() {
    private val key = "SEARCH"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        val title = activity?.findViewById<TextView>(R.id.fragment_name)

        title?.text = getString(R.string.search)
        setupBar(key, toolbar)
        setupActionsBar(key, view, toolbar)
        return view
    }
}