package com.example.adsimpl.localDb.sharedPref

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class AppSharedPref {

    private lateinit var appSharedPref: SharedPreferences

    companion object {
        @Volatile
        private var INSTANCE: AppSharedPref? = null

        fun getInstance(context: Context): AppSharedPref {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppSharedPref().apply {
                    appSharedPref = context.applicationContext.getSharedPreferences(
                        "app_shared_pref",
                        Context.MODE_PRIVATE
                    )
                    INSTANCE = this
                }
            }
        }
    }

    fun putString(key: String, value: String) {
        appSharedPref.edit { putString(key, value) }
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return appSharedPref.getString(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        appSharedPref.edit { putBoolean(key, value) }
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return appSharedPref.getBoolean(key, defaultValue)
    }

    fun putInt(key: String, value: Int) {
        appSharedPref.edit { putInt(key, value) }
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return appSharedPref.getInt(key, defaultValue)
    }

    fun clear() {
        appSharedPref.edit { clear() }
    }

}