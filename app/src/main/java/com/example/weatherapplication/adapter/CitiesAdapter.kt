package com.example.weatherapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.model.cities.Geoname
import com.example.weatherapplication.search.SearchFragmentDirections
import kotlinx.android.synthetic.main.city_row.view.*

class CitiesAdapter(private val citiesList: List<Geoname>) :
    RecyclerView.Adapter<CitiesAdapter.CitiesViewHolder>() {

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
            val action = SearchFragmentDirections.actionSearchFragmentToFavoritesFragment(
                name = geoName.name,
                adminName1 = geoName.adminName1,
                countryName = geoName.countryName,
                latitude = geoName.latitude,
                longitude = geoName.longitude
            )
            Navigation.findNavController(holder.itemView).navigate(action)
        }
    }

    override fun getItemCount() = citiesList.size
}