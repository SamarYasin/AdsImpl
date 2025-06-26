package com.example.adsimpl.app

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AdApplicationClass() : Application() {

    override fun onCreate() {
        super.onCreate()

        Log.d("Application Class", "onCreate: AdApplicationClass initialized")

    }
}