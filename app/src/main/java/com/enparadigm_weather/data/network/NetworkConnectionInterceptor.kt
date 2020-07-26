package com.kfd.esasyakshetra.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.kfd.esasyakshetra.util.NoInternetException
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit


class NetworkConnectionInterceptor(
    context: Context
) : Interceptor {

    private val applicationContext = context.applicationContext

    @RequiresApi(Build.VERSION_CODES.M)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInternetAvailable())
            throw NoInternetException("Make sure you have an active data connection")
        return chain.proceed(chain.request())
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.let {
            it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
        }
        return result
    }

    fun provideCacheInterceptor(): Interceptor? {
        return object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                var request: Request = chain.request()
                val originalResponse = chain.proceed(request)
                val cacheControl = originalResponse.header("Cache-Control")
                return if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains(
                        "no-cache"
                    ) ||
                    cacheControl.contains("must-revalidate") || cacheControl.contains("max-stale=0")
                ) {
                    val cc = CacheControl.Builder()
                        .maxStale(1, TimeUnit.DAYS)
                        .build()

                    /*return originalResponse.newBuilder()
                                        .header("Cache-Control", "public, max-stale=" + 60 * 60 * 24)
                                        .build();*/
                    request = request.newBuilder()
                        .cacheControl(cc)
                        .build()
                    chain.proceed(request)
                } else {
                    originalResponse
                }
            }
        }
    }

    fun provideOfflineCacheInterceptor(): Interceptor? {
        return object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                return try {
                    chain.proceed(chain.request())
                } catch (e: Exception) {
                    val cacheControl = CacheControl.Builder()
                        .onlyIfCached()
                        .maxStale(1, TimeUnit.DAYS)
                        .build()
                    val offlineRequest = chain.request().newBuilder()
                        .cacheControl(cacheControl)
                        .build()
                    chain.proceed(offlineRequest)
                }
            }
        }
    }

}