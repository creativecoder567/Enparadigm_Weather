package com.kfd.esasyakshetra.util

import android.content.Context
import android.content.SharedPreferences


class AppPref(context: Context) {

    private val applicationContext: Context
    var sEditor: SharedPreferences.Editor
    var sharedPreferences: SharedPreferences

    companion object {
        const val ASSIGNED_LOCATION_ID = "assigned_location_id"
        private const val SG_SHARED_PREFERENCE = "app-pref.xml"
        private var instance: AppPref? = null

        fun init(context: Context): AppPref? {

            if (instance == null) {
                synchronized(AppPref::class.java) {
                    if (instance == null) {
                        instance = AppPref(context)
                    }
                }
            }
            return instance
        }

        fun getInstance(): AppPref? {
            if (instance == null) {
                synchronized(AppPref::class.java) { throw IllegalArgumentException("not init") }
            }
            return instance
        }
    }

    init {
        applicationContext = context.applicationContext
        sharedPreferences = applicationContext.getSharedPreferences(
            SG_SHARED_PREFERENCE,
            Context.MODE_PRIVATE
        )
        sEditor = sharedPreferences.edit()

    }

    fun getString(key: String?, nothing: Nothing?): String? {
        return sharedPreferences.getString(key, "")
    }

    fun putString(key: String?, value: String?) {
        sEditor.putString(key, value)
        sEditor.commit()
    }

    fun getBool(key: String?): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun putBool(key: String?, value: Boolean) {
        sEditor.putBoolean(key, value)
        sEditor.commit()
    }

    fun getInt(key: String?): Int {
        return sharedPreferences.getInt(key, 0)
    }

    fun putInt(key: String?, value: Int) {
        sEditor.putInt(key, value)
        sEditor.commit()
    }


    val locationId: Int
        get() = getInt(ASSIGNED_LOCATION_ID)

    fun clearAll() {
        sEditor.clear().commit()
    }


}