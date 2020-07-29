package com.enparadigm_weather.data.repository

import com.enparadigm_weather.data.network.MyApi
import com.enparadigm_weather.model.daily.DailyWeather
import com.enparadigm_weather.util.Constants
import com.kfd.esasyakshetra.data.network.SafeApiRequest

class Repository(val api: MyApi) : SafeApiRequest() {

    suspend fun searchCity(city: String): DailyWeather {
        return apiRequest { api.getDailyWeather(city, Constants.API_KEY) }
    }
}