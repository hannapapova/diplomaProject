package com.example.weatherapplication.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.weatherapplication.*
import com.example.weatherapplication.adapter.FavouriteCitiesAdapter
import com.example.weatherapplication.koin.WeatherViewModel
import com.example.weatherapplication.model.cities.Geoname
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

        if (viewModel.selectedCity.value != null) {
            viewModel.setSelectedAsCurrent()
        }

        viewModel.favouriteCities.observe(viewLifecycleOwner, {
            recycler_favourites.adapter = FavouriteCitiesAdapter(it, viewModel)
        })

        viewModel.currentCity.observe(viewLifecycleOwner, {
            val selectedLocationName: String = if (it != null)
                "${it?.name}, ${it?.adminName1}, ${it?.countryName}"  else ""
            tv_city_selected.text = selectedLocationName
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.favouriteCities.value?.let { viewModel.deleteNotFavouritesFromDB(it) }
    }
}