package com.enparadigm_weather.data.network

import com.enparadigm_weather.WeatherApplication
import com.enparadigm_weather.model.daily.DailyWeather
import com.kfd.esasyakshetra.data.network.NetworkConnectionInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.File
import java.util.concurrent.TimeUnit

interface MyApi {

    @GET("forecast/daily")
    fun getDailyWeather(
        @Query("city") city: String?,
        @Query("key") key: String?
    ): Call<DailyWeather>

    companion object {
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor): MyApi {

            var file = File(WeatherApplication.appContext.getCacheDir(), "weather_cache")
            var cache = Cache(file, 10 * 1024 * 1024);

            val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addNetworkInterceptor {
                    var request = it.request();
                    var response = it.proceed(request);
                    var maxAge = 60 * 60 * 36;
                    response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
                }
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .cache(cache)
//                .addInterceptor(networkConnectionInterceptor)
//                .addInterceptor(networkConnectionInterceptor.provideCacheInterceptor()!!)
//                .addInterceptor(networkConnectionInterceptor.provideOfflineCacheInterceptor()!!)
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