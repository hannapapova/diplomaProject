package com.example.weatherapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.koin.WeatherViewModel
import com.example.weatherapplication.model.cities.Geoname
import kotlinx.android.synthetic.main.city_row.view.*

class CitiesAdapter(
    private val citiesList: List<Geoname>,
    private val viewModel: WeatherViewModel
) :
    RecyclerView.Adapter<CitiesAdapter.CitiesViewHolder>() {

    class CitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val location: TextView = itemView.tv_city_selected
        val imageView: ImageView = itemView.iv_star_city
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

        viewModel.favouriteCities.value?.forEach { favourite ->
            if (geoName.name == favourite.name &&
                geoName.adminName1 == favourite.adminName1 &&
                geoName.countryName == favourite.countryName &&
                geoName.latitude == favourite.latitude &&
                geoName.longitude == favourite.longitude
            ) {
                if (favourite.inFavourites)
                    geoName.inFavourites = true
            }
        }

        holder.imageView.setBackgroundResource(setImageView(geoName))

        holder.itemView.setOnClickListener {
            viewModel.setSelectedCity(geoName)
            Navigation.findNavController(holder.itemView).popBackStack()
        }

        holder.itemView.iv_star_city.setOnClickListener {
            geoName.inFavourites = !geoName.inFavourites
            it.setBackgroundResource(setImageView(geoName))
        }
    }

    override fun getItemCount() = citiesList.size

    private fun setImageView(geoname: Geoname): Int {
        return when (geoname.inFavourites) {
            true -> {
                R.drawable.ic_star_filled
            }
            false -> {
                R.drawable.ic_star_empty
            }
        }
    }
}