package com.example.adsimpl.app

import android.app.Application
import android.util.Log
import com.example.adsimpl.adManager.OpenAppAdManager
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AdApplicationClass() : Application() {

    private lateinit var openAppAdManager: OpenAppAdManager

    override fun onCreate() {
        super.onCreate()
        Log.d("Application Class", "onCreate: AdApplicationClass initialized")
        MobileAds.initialize(this@AdApplicationClass){}
        openAppAdManager = OpenAppAdManager(this)
    }
}