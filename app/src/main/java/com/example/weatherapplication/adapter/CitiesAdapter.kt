package com.example.weatherapplication.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.koin.WeatherViewModel
import com.example.weatherapplication.model.cities.Geoname
import com.example.weatherapplication.room.entity.CurrentCity
import kotlinx.android.synthetic.main.city_row.view.*

class CitiesAdapter(private val citiesList: List<Geoname>, val viewModel: WeatherViewModel) :
    RecyclerView.Adapter<CitiesAdapter.CitiesViewHolder>() {

//    private val viewModel by inject(WeatherViewModel::class.java)

    class CitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val location: TextView = itemView.tv_city_favourites
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.city_row, parent, false)
        return CitiesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        val geoName = citiesList[position]
        val locationName = "${geoName.name}, ${geoName.adminName1}, ${geoName.countryName}"
        holder.location.text = locationName

        holder.itemView.setOnClickListener {
//            val action = SearchFragmentDirections.actionSearchFragmentToFavoritesFragment(
//                name = geoName.name,
//                adminName1 = geoName.adminName1,
//                countryName = geoName.countryName,
//                latitude = geoName.latitude,
//                longitude = geoName.longitude
//            )
//            Navigation.findNavController(holder.itemView).navigate(action)
//            Log.d("viewModel", viewModel.currentWeather.toString())
            val currentCity = CurrentCity(
                geoName.name,
                geoName.adminName1,
                geoName.countryName,
                geoName.latitude,
                geoName.longitude
            )
            Log.d("viewModel", "adapter current city $currentCity")
            Log.d("viewModel", "adapter selected city before put " + viewModel.selectedCity.value.toString())
            viewModel.putSelectedCity(geoName)
            Log.d(
                "viewModel",
                "adapter selected city after put " + viewModel.selectedCity.value.toString()
            )
            Navigation.findNavController(holder.itemView).popBackStack()


//            viewModel.deleteCurrentCityTable()
//            viewModel.insertCurrentCity(currentCity)
//            Log.d("viewModel", viewModel.currentWeather.toString())
//            Log.d("viewModel", viewModel.repository.savedCurrentCity.value.toString())
//            Log.d("viewModel", viewModel.currentCity.value.toString())
        }
    }

    override fun getItemCount() = citiesList.size
}