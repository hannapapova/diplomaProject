package com.example.weatherapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.home.getStatusImage
import com.example.weatherapplication.room.entity.SavedHourlyWeather
import java.text.SimpleDateFormat
import java.util.*

class HourlyAdapter(private val weatherList: List<SavedHourlyWeather>?) :
    RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {

    class HourlyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time: TextView = itemView.findViewById(R.id.rv_time_hourly)
        val picture: ImageView = itemView.findViewById(R.id.rv_icon_hourly)
        val temperature: TextView = itemView.findViewById(R.id.rv_temperature_hourly)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.hourly_row, parent, false)
        return HourlyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val currentItem = weatherList?.get(position)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        val time = Date(currentItem?.dateTime.toString().plus("000").toLong())

        holder.time.text = timeFormat.format(time)
        holder.picture.setImageResource(getStatusImage(currentItem?.weatherMain))
        holder.temperature.text = currentItem?.temperature?.toInt().toString().plus("°C")
    }

    override fun getItemCount() = 24
}