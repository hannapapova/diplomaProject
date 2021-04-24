package com.example.weatherapplication.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.weatherapplication.*
import com.example.weatherapplication.koin.WeatherViewModel
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.koin.android.ext.android.inject

class FavoritesFragment : Fragment() {
    private val key = "FAVORITES"
    private val viewModel by inject<WeatherViewModel>()
//    private val args: FavoritesFragmentArgs by navArgs()

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

//        toolbar?.setNavigationOnClickListener {
//            val action = FavoritesFragmentDirections.actionFavoritesFragmentToHomeFragment(
//                name = args.name,
//                adminName1 = args.adminName1,
//                countryName = args.countryName,
//                latitude = args.latitude,
//                longitude = args.longitude
//            )
//            Navigation.findNavController(view).navigate(action)
//        }
//        toolbar?.setOnMenuItemClickListener {
//            Navigation.findNavController(view)
//                .navigate(R.id.favoritesFragment_to_searchFragment)
//            true
//        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.getResult(
//            Geoname(
//                args.adminName1,
//                args.countryName,
//                args.latitude,
//                args.longitude,
//                args.name
//            )
//        )

//        Log.d("viewmodel", "onViewCreated")

        Log.d("viewModel", "favourites selected city " + viewModel.selectedCity.value.toString())
        Log.d(
            "viewModel",
            "favourites repo saved city " + viewModel.repository.savedCurrentCity.value.toString()
        )
        Log.d("viewModel", "favourites current city " + viewModel.currentCity.value.toString())

        Log.d("viewmodel", "favourites before putSelectedIntoRepo: " + viewModel.currentCity.value)
        if (viewModel.selectedCity.value != null) {
            viewModel.putSelectedIntoRepo()
            viewModel.putSelectedIntoFavourites()
            Log.d(
                "viewmodel",
                "favourites after putSelectedIntoRepo: " + viewModel.currentCity.value
            )
        }

        viewModel.favouriteCities.observe(viewLifecycleOwner, {
            tv_city_favourites.text = "Favourite: " + it?.toString() ?: "wait"
        })

        viewModel.currentCity.observe(viewLifecycleOwner, {
            tv_current.text = "Current: " + it?.toString() ?: "wait"
        })
    }
}