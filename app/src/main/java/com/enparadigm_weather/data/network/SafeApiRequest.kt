package com.kfd.esasyakshetra.data.network

import android.util.Log
import com.kfd.esasyakshetra.util.ApiException
import retrofit2.Call

abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Call<T>): T {
        val response = call.invoke().execute()

        if (response.isSuccessful) {
            Log.d("sarath", response.message())
//            Log.d("sarath", response.body().toString())
            return response.body()!!
        } else {

            val message = StringBuilder()
            message.append("Error code: ${response.code()}")
            throw ApiException(message.toString())
        }

    }
}
