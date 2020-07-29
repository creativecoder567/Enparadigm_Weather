package com.enparadigm_weather.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.NonNull
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
                "a01d" -> context.resources.getDrawable(R.drawable.a01d)
                "a01n" -> context.resources.getDrawable(R.drawable.a01n)
                "a02d" -> context.resources.getDrawable(R.drawable.a02d)
                "a02n" -> context.resources.getDrawable(R.drawable.a02n)
                "a03d" -> context.resources.getDrawable(R.drawable.a03d)
                "a03n" -> context.resources.getDrawable(R.drawable.a03n)
                "a04d" -> context.resources.getDrawable(R.drawable.a04d)
                "a04n" -> context.resources.getDrawable(R.drawable.a04n)
                "a05d" -> context.resources.getDrawable(R.drawable.a05d)
                "a05n" -> context.resources.getDrawable(R.drawable.a05n)
                "c01d" -> context.resources.getDrawable(R.drawable.c01d)
                "c01n" -> context.resources.getDrawable(R.drawable.c01n)
                "c02d" -> context.resources.getDrawable(R.drawable.c02d)
                "c02n" -> context.resources.getDrawable(R.drawable.c02n)
                "c03d" -> context.resources.getDrawable(R.drawable.c03d)
                "c03n" -> context.resources.getDrawable(R.drawable.c03n)
                "c04d" -> context.resources.getDrawable(R.drawable.c04d)
                "c04n" -> context.resources.getDrawable(R.drawable.c04n)
                "d01d" -> context.resources.getDrawable(R.drawable.d01d)
                "d01n" -> context.resources.getDrawable(R.drawable.d01n)
                "d02d" -> context.resources.getDrawable(R.drawable.d02d)
                "d02n" -> context.resources.getDrawable(R.drawable.d02n)
                "d03d" -> context.resources.getDrawable(R.drawable.d03d)
                "d03n" -> context.resources.getDrawable(R.drawable.d03n)
                "f01d" -> context.resources.getDrawable(R.drawable.f01d)
                "f01n" -> context.resources.getDrawable(R.drawable.f01n)
                "r01d" -> context.resources.getDrawable(R.drawable.r01d)
                "r01n" -> context.resources.getDrawable(R.drawable.r01n)
                "r02d" -> context.resources.getDrawable(R.drawable.r02d)
                "r02n" -> context.resources.getDrawable(R.drawable.r02n)
                "r03d" -> context.resources.getDrawable(R.drawable.r03d)
                "r03n" -> context.resources.getDrawable(R.drawable.r03n)
                "r04d" -> context.resources.getDrawable(R.drawable.r04d)
                "r04n" -> context.resources.getDrawable(R.drawable.r04n)
                "r05d" -> context.resources.getDrawable(R.drawable.r05d)
                "r05n" -> context.resources.getDrawable(R.drawable.r05n)
                "r06d" -> context.resources.getDrawable(R.drawable.r06d)
                "r06n" -> context.resources.getDrawable(R.drawable.r06n)
                "t01d" -> context.resources.getDrawable(R.drawable.t01d)
                "t01n" -> context.resources.getDrawable(R.drawable.t01n)
                "t02d" -> context.resources.getDrawable(R.drawable.t02d)
                "t02n" -> context.resources.getDrawable(R.drawable.t02n)
                "t03d" -> context.resources.getDrawable(R.drawable.t03d)
                "t03n" -> context.resources.getDrawable(R.drawable.t03n)
                "t04d" -> context.resources.getDrawable(R.drawable.t04d)
                "t04n" -> context.resources.getDrawable(R.drawable.t04n)
                "t05d" -> context.resources.getDrawable(R.drawable.t05d)
                "t05n" -> context.resources.getDrawable(R.drawable.t05n)
                else -> context.resources.getDrawable(R.drawable.c01d)
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




