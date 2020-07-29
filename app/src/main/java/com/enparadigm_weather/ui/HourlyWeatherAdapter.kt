package com.enparadigm_weather.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.enparadigm_weather.R
import com.enparadigm_weather.model.daily.Data
import com.enparadigm_weather.util.BindingUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class HourlyWeatherAdapter(
    private val context: Context,
    data: List<Data>,
    isListView: Boolean
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val inflater: LayoutInflater
    private val data: List<Data>
    private val isListView: Boolean
    private var hourlyWeatherViewHolder: HourlyWeatherViewHolder? = null
    private var hourlyWeatherListViewHolder: HourlyWeatherListViewHolder? =
        null
    var i = 0
    init {
        inflater = LayoutInflater.from(context)
        this.data = data
        this.isListView = isListView
    }

    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (isListView) {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_hourly_weather_list, parent, false)
            HourlyWeatherListViewHolder(view)
        } else {
            val view: View =
                inflater.inflate(R.layout.adapter_hourly_weather_item, parent, false)
            HourlyWeatherViewHolder(view)
        }
    }

    override fun onBindViewHolder(@NonNull holder: RecyclerView.ViewHolder, position: Int) {
        val hourlyDatum: Data = data[holder.getAdapterPosition()]
        if (isListView) {
            hourlyWeatherListViewHolder = holder as HourlyWeatherListViewHolder
            hourlyWeatherListViewHolder!!.bindView(hourlyDatum)
        } else {
            hourlyWeatherViewHolder = holder as HourlyWeatherViewHolder
            hourlyWeatherViewHolder!!.bindView(hourlyDatum)
        }
    }


    override fun getItemCount(): Int {
        return data.size
    }


    internal inner class HourlyWeatherViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var textViewTime: TextView? = itemView.findViewById(R.id.time)

        var textViewTemperature: TextView? = itemView.findViewById(R.id.temperature)

        var imageViewIcon: ImageView? = itemView.findViewById(R.id.icon)
        fun bindView(hourlyDatum: Data) {
            var date: Date? = null
            @SuppressLint("SimpleDateFormat") val f =
                SimpleDateFormat("yyy-MM-dd:HH")
            try {
                /*no free call for hourly based api*/
//                date = f.parse(hourlyDatum.datetime)
                @SuppressLint("SimpleDateFormat") val now =
                    SimpleDateFormat("h aa").format(System.currentTimeMillis())
                textViewTime!!.text = now
                i++
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            textViewTemperature?.setText(BindingUtils.convertDegree(hourlyDatum.temp.toFloat()))
            imageViewIcon!!.setImageDrawable(
                BindingUtils.getWeatherIcon(
                    context,
                    hourlyDatum.weather.icon
                )
            )
        }

    }

    internal inner class HourlyWeatherListViewHolder(view: View?) :
        RecyclerView.ViewHolder(view!!) {
        var textViewTime: TextView? = itemView.findViewById(R.id.time)

        var imageViewWeatherIcon: ImageView? = itemView.findViewById(R.id.icon)

        var textViewWeatherTemperature: TextView? = itemView.findViewById(R.id.temperature)

        var textViewWindDirection: TextView? = null
        fun bindView(hourlyDatum: Data) {
            var date: Date?
            @SuppressLint("SimpleDateFormat") val f =
                SimpleDateFormat("yyy-MM-dd:HH")
            try {
                date = f.parse(hourlyDatum.datetime)
                @SuppressLint("SimpleDateFormat") val now =
                    SimpleDateFormat("h aa").format(date)
                textViewTime!!.text = now
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            textViewWeatherTemperature?.setText(BindingUtils.convertDegree(hourlyDatum.temp.toFloat()))
            textViewWindDirection?.setText(
                hourlyDatum.wind_cdir + " " + hourlyDatum.wind_spd
                        + " m/s"
            )
            imageViewWeatherIcon!!.setImageDrawable(
                BindingUtils.getWeatherIcon(
                    context,
                    hourlyDatum.weather.icon
                )
            )
        }


    }


}
