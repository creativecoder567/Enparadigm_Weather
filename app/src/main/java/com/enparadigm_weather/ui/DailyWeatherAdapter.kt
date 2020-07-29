package com.enparadigm_weather.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enparadigm_weather.R
import com.enparadigm_weather.model.daily.Data
import com.enparadigm_weather.util.BindingUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DailyWeatherAdapter(
    private val context: Context,
    data: List<Data>,
    isListView: Boolean
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val inflater: LayoutInflater
    private val data: List<Data>
    private val isListView: Boolean
    private var dailyWeatherListViewHolder: DailyWeatherListViewHolder? = null
    private var dailyWeatherViewHolder: DailyWeatherViewHolder? = null

    init {
        inflater = LayoutInflater.from(context)
        this.data = data
        this.isListView = isListView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isListView) {
            val view =
                inflater.inflate(R.layout.adapter_daily_weather_list_item, parent, false)
            DailyWeatherListViewHolder(view)
        } else {
            val view =
                inflater.inflate(R.layout.adapter_daily_weather_item, parent, false)
            DailyWeatherViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dailyDatum: Data = data[holder.getAdapterPosition()]
        if (isListView) {
            dailyWeatherListViewHolder = holder as DailyWeatherListViewHolder
            dailyWeatherListViewHolder!!.bindView(dailyDatum)
        } else {
            dailyWeatherViewHolder = holder as DailyWeatherViewHolder
            dailyWeatherViewHolder!!.bindView(dailyDatum)
        }
    }



    internal inner class DailyWeatherViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var textViewTime: TextView? = itemView.findViewById(R.id.day)
        var textViewMaxTemperature: TextView? = itemView.findViewById(R.id.max_temperature)
        var textViewMinTemperature: TextView? = itemView.findViewById(R.id.min_temperature)
        var imageViewIcon: ImageView? = itemView.findViewById(R.id.icon)

        fun bindView(dailyDatum: Data) {
            imageViewIcon!!.setImageDrawable(
                BindingUtils.getWeatherIcon(
                    context,
                    dailyDatum.weather.icon
                )
            )
            @SuppressLint("SimpleDateFormat") var f =
                SimpleDateFormat("yyy-MM-dd")
            try {
                val d = f.parse(dailyDatum.datetime)
                f = SimpleDateFormat("E")
                textViewTime!!.text = f.format(d)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            textViewMaxTemperature?.setText(BindingUtils.convertDegree(dailyDatum.max_temp.toFloat()))
            textViewMinTemperature?.setText(BindingUtils.convertDegree(dailyDatum.min_temp.toFloat()))
        }


    }

    internal inner class DailyWeatherListViewHolder(view: View?) :
        RecyclerView.ViewHolder(view!!) {
        var textViewTime: TextView? = view?.findViewById(R.id.day)

        var textViewMaxTemperature: TextView? = view?.findViewById(R.id.max_temperature)

        var textViewMinTemperature: TextView? = view?.findViewById(R.id.min_temperature)

        var imageViewIcon: ImageView? = view?.findViewById(R.id.icon)

        var textViewSunrise: TextView? = view?.findViewById(R.id.sunrise)

        var textViewSunset: TextView? = view?.findViewById(R.id.sunset)

        var textViewDateAndMonth: TextView? = view?.findViewById(R.id.date_month)

        var textViewClimate: TextView? = view?.findViewById(R.id.climate)

        var textViewWindDirection: TextView? = view?.findViewById(R.id.wind_direction_speed)

        fun bindView(dailyDatum: Data) {
            imageViewIcon!!.setImageDrawable(
                BindingUtils.getWeatherIcon(
                    context,
                    dailyDatum.weather.icon
                )
            )
            @SuppressLint("SimpleDateFormat") var f =
                SimpleDateFormat("yyy-MM-dd")
            try {
                val d = f.parse(dailyDatum.datetime)
                f = SimpleDateFormat("EEEE")
                textViewTime!!.text = f.format(d).toUpperCase()
                f = SimpleDateFormat("MMMM dd")
                textViewDateAndMonth!!.text = f.format(d).toUpperCase()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val sunriseTime =
                Date(TimeUnit.SECONDS.toMillis(dailyDatum.sunrise_ts.toLong()))
            val sunrise = SimpleDateFormat("hh:mm aa").format(sunriseTime)
            textViewSunrise!!.text = sunrise
            val sunsetTime =
                Date(TimeUnit.SECONDS.toMillis(dailyDatum.sunset_ts.toLong()))
            val sunset = SimpleDateFormat("hh:mm aa").format(sunsetTime)
            textViewSunset!!.text = sunset.toString()
            textViewMaxTemperature?.setText(BindingUtils.convertDegree(dailyDatum.max_temp.toFloat()))
            textViewClimate?.setText(dailyDatum.weather.description)
            textViewWindDirection?.setText(
                dailyDatum.wind_cdir + dailyDatum.wind_spd
            )
            textViewMinTemperature?.setText(BindingUtils.convertDegree(dailyDatum.min_temp.toFloat()))
        }

    }


}
