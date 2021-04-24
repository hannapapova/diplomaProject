package com.example.weatherapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.koin.WeatherViewModel
import com.example.weatherapplication.model.cities.Geoname
import com.example.weatherapplication.room.entity.FavouriteCity
import kotlinx.android.synthetic.main.favourite_city_row.view.*

class FavouriteCitiesAdapter(
    private val favouriteCitiesList: List<FavouriteCity>,
    private val viewModel: WeatherViewModel
) : RecyclerView.Adapter<FavouriteCitiesAdapter.FavouriteCitiesViewHolder>() {

    class FavouriteCitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val location: TextView = itemView.tv_city_selected
        val imageView: ImageView = itemView.iv_star_favourites
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteCitiesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.favourite_city_row, parent, false)
        return FavouriteCitiesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteCitiesViewHolder, position: Int) {
        val favouriteCity = favouriteCitiesList[position]
        val locationName =
            "${favouriteCity.name}, ${favouriteCity.adminName1}, ${favouriteCity.countryName}"
        holder.location.text = locationName
        holder.imageView.setBackgroundResource(setImageView(favouriteCity))

        holder.itemView.iv_star_favourites.setOnClickListener {
            favouriteCity.inFavourites = !favouriteCity.inFavourites
            it.setBackgroundResource(setImageView(favouriteCity))
        }

        holder.itemView.setOnClickListener {
            viewModel.setSelectedCity(
                Geoname(
                    favouriteCity.adminName1,
                    favouriteCity.countryName,
                    favouriteCity.latitude,
                    favouriteCity.longitude,
                    favouriteCity.name
                )
            )
            viewModel.setSelectedAsCurrent()
        }
    }

    override fun getItemCount() = favouriteCitiesList.size

    private fun setImageView(favouriteCity: FavouriteCity): Int {
        return when (favouriteCity.inFavourites) {
            true -> {
                R.drawable.ic_star_filled
            }
            false -> {
                R.drawable.ic_star_empty
            }
        }
    }
}