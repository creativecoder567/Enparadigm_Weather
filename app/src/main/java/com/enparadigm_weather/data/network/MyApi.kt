package com.enparadigm_weather.data.network

import com.enparadigm_weather.WeatherApplication
import com.enparadigm_weather.model.daily.DailyWeather
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.File

interface MyApi {

    @GET("forecast/daily")
    fun getDailyWeather(
        @Query("city") city: String?,
        @Query("key") key: String?
    ): Call<DailyWeather>

    companion object {
        operator fun invoke(): MyApi {

            val file = File(WeatherApplication.appContext.getCacheDir(), "weather_cache")
            val cache = Cache(file, 10 * 1024 * 1024)

            val okHttpClient = OkHttpClient().newBuilder()
                .addNetworkInterceptor {
                    val request = it.request()
                    val response = it.proceed(request)
                    val maxAge = 60 * 60 * 24
                    response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build()
                }
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .cache(cache)
                .build()
            return Retrofit.Builder()

                .baseUrl("https://api.weatherbit.io/v2.0/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(MyApi::class.java)

        }
    }
}