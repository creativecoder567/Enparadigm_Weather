package com.enparadigm_weather.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.enparadigm_weather.R
import java.text.SimpleDateFormat
import java.util.*

class BindingUtils {


    public companion object {

        private val degree = '\u00b0'

        fun concatString(one: String?, two: String?): String? {
            return if (one != null && two != null) "$one, $two" else ""
        }

        fun getWeatherIcon(context: Context, code: String?): Drawable? {
            return when (code) {
                "a01d" -> ContextCompat.getDrawable(context, R.drawable.a01d)
                "a01n" -> ContextCompat.getDrawable(context, R.drawable.a01n)
                "a02d" -> ContextCompat.getDrawable(context, R.drawable.a02d)
                "a02n" -> ContextCompat.getDrawable(context, R.drawable.a02n)
                "a03d" -> ContextCompat.getDrawable(context, R.drawable.a03d)
                "a03n" -> ContextCompat.getDrawable(context, R.drawable.a03n)
                "a04d" -> ContextCompat.getDrawable(context, R.drawable.a04d)
                "a04n" -> ContextCompat.getDrawable(context, R.drawable.a04n)
                "a05d" -> ContextCompat.getDrawable(context, R.drawable.a05d)
                "a05n" -> ContextCompat.getDrawable(context, R.drawable.a05n)
                "c01d" -> ContextCompat.getDrawable(context, R.drawable.c01d)
                "c01n" -> ContextCompat.getDrawable(context, R.drawable.c01n)
                "c02d" -> ContextCompat.getDrawable(context, R.drawable.c02d)
                "c02n" -> ContextCompat.getDrawable(context, R.drawable.c02n)
                "c03d" -> ContextCompat.getDrawable(context, R.drawable.c03d)
                "c03n" -> ContextCompat.getDrawable(context, R.drawable.c03n)
                "c04d" -> ContextCompat.getDrawable(context, R.drawable.c04d)
                "c04n" -> ContextCompat.getDrawable(context, R.drawable.c04n)
                "d01d" -> ContextCompat.getDrawable(context, R.drawable.d01d)
                "d01n" -> ContextCompat.getDrawable(context, R.drawable.d01n)
                "d02d" -> ContextCompat.getDrawable(context, R.drawable.d02d)
                "d02n" -> ContextCompat.getDrawable(context, R.drawable.d02n)
                "d03d" -> ContextCompat.getDrawable(context, R.drawable.d03d)
                "d03n" -> ContextCompat.getDrawable(context, R.drawable.d03n)
                "f01d" -> ContextCompat.getDrawable(context, R.drawable.f01d)
                "f01n" -> ContextCompat.getDrawable(context, R.drawable.f01n)
                "r01d" -> ContextCompat.getDrawable(context, R.drawable.r01d)
                "r01n" -> ContextCompat.getDrawable(context, R.drawable.r01n)
                "r02d" -> ContextCompat.getDrawable(context, R.drawable.r02d)
                "r02n" -> ContextCompat.getDrawable(context, R.drawable.r02n)
                "r03d" -> ContextCompat.getDrawable(context, R.drawable.r03d)
                "r03n" -> ContextCompat.getDrawable(context, R.drawable.r03n)
                "r04d" -> ContextCompat.getDrawable(context, R.drawable.r04d)
                "r04n" -> ContextCompat.getDrawable(context, R.drawable.r04n)
                "r05d" -> ContextCompat.getDrawable(context, R.drawable.r05d)
                "r05n" -> ContextCompat.getDrawable(context, R.drawable.r05n)
                "r06d" -> ContextCompat.getDrawable(context, R.drawable.r06d)
                "r06n" -> ContextCompat.getDrawable(context, R.drawable.r06n)
                "t01d" -> ContextCompat.getDrawable(context, R.drawable.t01d)
                "t01n" -> ContextCompat.getDrawable(context, R.drawable.t01n)
                "t02d" -> ContextCompat.getDrawable(context, R.drawable.t02d)
                "t02n" -> ContextCompat.getDrawable(context, R.drawable.t02n)
                "t03d" -> ContextCompat.getDrawable(context, R.drawable.t03d)
                "t03n" -> ContextCompat.getDrawable(context, R.drawable.t03n)
                "t04d" -> ContextCompat.getDrawable(context, R.drawable.t04d)
                "t04n" -> ContextCompat.getDrawable(context, R.drawable.t04n)
                "t05d" -> ContextCompat.getDrawable(context, R.drawable.t05d)
                "t05n" -> ContextCompat.getDrawable(context, R.drawable.t05n)
                else -> ContextCompat.getDrawable(context, R.drawable.c01d)
            }
        }

        fun convertDegree(@NonNull temperature: Float?): String? {
            return if (temperature != null) Math.round(temperature)
                .toString() + degree + "c" else ""
        }

        fun getBackgroundImage(): Int {
            val time =
                SimpleDateFormat("kk").format(Date().time)
            Log.d("zzz", "Time: $time")
            val currentTime = Integer.valueOf(time)
            return if (currentTime >= 6 && currentTime <= 10) {
                R.drawable.morning_sky
            } else if (currentTime >= 11 && currentTime <= 16) {
                R.drawable.noon_sky
            } else if (currentTime >= 17 && currentTime <= 19) {
                R.drawable.evening_sky
            } else {
                R.drawable.night_sky
            }
        }
    }

}




