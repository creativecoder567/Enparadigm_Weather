package com.enparadigm_weather.data.network

import com.kfd.esasyakshetra.data.network.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface MyApi {

    companion object {
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor): MyApi {

            val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addInterceptor(networkConnectionInterceptor)
                .addInterceptor(networkConnectionInterceptor.provideCacheInterceptor()!!)
                .addInterceptor(networkConnectionInterceptor.provideOfflineCacheInterceptor()!!)
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