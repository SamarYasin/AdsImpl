package com.example.adsimpl.adManager

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import javax.inject.Inject

class InterstitialAdManager @Inject constructor() {

    private var cachedInterstitialAd: InterstitialAd? = null

    fun fetchAd(context: Context, adUnitId: String, onAdFetched: (() -> Unit)? = null) {
        InterstitialAd.load(
            context,
            adUnitId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d("InterstitialAdManager", "Interstitial ad loaded")
                    cachedInterstitialAd = interstitialAd
                    onAdFetched?.invoke()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("InterstitialAdManager", "Interstitial ad failed to load: ${error.message}")
                    cachedInterstitialAd = null
                }
            }
        )
    }

    fun showAd(activity: Activity, onAdDismissed: (() -> Unit)? = null) {
        cachedInterstitialAd?.let { interstitialAd ->
            interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback(){
                override fun onAdDismissedFullScreenContent() {
                    Log.d("InterstitialAdManager", "Interstitial ad dismissed")
                    cachedInterstitialAd = null
                    onAdDismissed?.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    Log.e("InterstitialAdManager", "Interstitial ad failed to show: ${error.message}")
                    cachedInterstitialAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d("InterstitialAdManager", "Interstitial ad showed")
                }
            }
            interstitialAd.show(activity)
        } ?: Log.d("InterstitialAdManager", "No interstitial ad to show")
    }

}