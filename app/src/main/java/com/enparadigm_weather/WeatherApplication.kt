package com.enparadigm_weather

import android.app.Application
import com.kfd.esasyakshetra.util.AppPref

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppPref.init(this)
        appContext = this

    }

    companion object {
        lateinit var appContext: Application
    }


}