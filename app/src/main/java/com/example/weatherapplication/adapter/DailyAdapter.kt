package com.example.weatherapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.model.DailyWeather
import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter(private val weatherList: List<DailyWeather>) :
    RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {

    class DailyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.rv_date_daily)
        val day: TextView = itemView.findViewById(R.id.rv_day_daily)
        val picture: ImageView = itemView.findViewById(R.id.rv_icon_daily)
        val dayTemp: TextView = itemView.findViewById(R.id.rv_day_temp_daily)
        val nightTemp: TextView = itemView.findViewById(R.id.rv_night_temp_daily)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.daily_row, parent, false)

        return DailyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val currentItem = weatherList[position]
        val dataMonthFormat = SimpleDateFormat("MMMM dd", Locale.ENGLISH)
        val dataDayFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
        val currentDay = Date(currentItem.dateTime.toString().plus("000").toLong())
        val currentMonth = Date(currentItem.dateTime.toString().plus("000").toLong())

        holder.date.text = dataMonthFormat.format(currentMonth)
        holder.day.text = dataDayFormat.format(currentDay)
        holder.picture.setImageResource(getImage(currentItem.weather[0].main))
        holder.dayTemp.text = currentItem.temperature.day.toInt().toString().plus("°C")
        holder.nightTemp.text = currentItem.temperature.night.toInt().toString().plus("°C")
    }

    override fun getItemCount() = weatherList.size
}

internal fun getImage(status: String): Int {
    return when (status) {
        "Thunderstorm" -> R.drawable.severe_thunderstorm
        "Drizzle" -> R.drawable.drizzle
        "Rain" -> R.drawable.rain
        "Snow" -> R.drawable.snow
        "Mist", "Smoke", "Fog" -> R.drawable.fog
        "Haze", "Dust", "Sand", "Ash" -> R.drawable.dust
        "Squall", "Tornado" -> R.drawable.tornado
        "Clear" -> R.drawable.mostly_sunny
        "Clouds" -> R.drawable.mostly_cloudy
        else -> R.drawable.mostly_sunny
    }
}