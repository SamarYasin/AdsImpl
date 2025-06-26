package com.example.adsimpl.adManager

import android.app.Activity
import android.util.Log
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import javax.inject.Inject

class BannerAdManager @Inject constructor() {

    fun loadBannerAd(activity: Activity, adView: AdView) {
        Log.d("BannerAdManager", "Loading banner ad")
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    fun showBannerAd(adView: AdView) {
        Log.d("BannerAdManager", "Showing banner ad")
        adView.visibility = View.VISIBLE
    }

    fun hideBannerAd(adView: AdView) {
        Log.d("BannerAdManager", "Hiding banner ad")
        adView.visibility = View.GONE
    }

}