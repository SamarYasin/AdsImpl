package com.example.adsimpl.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.adsimpl.adManager.AdTimer
import com.example.adsimpl.adManager.InterstitialAdManager
import com.example.adsimpl.network.NetworkHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InterstitialViewModel @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val adTimer: AdTimer,
    private val interstitialAdManager: InterstitialAdManager
) : ViewModel() {

    private var canShowAd = true

    fun preFetchInterstitialAd(
        context: Context,
        adUnitId: String,
        onAdFetched: (() -> Unit)? = null
    ) {
        when (networkHandler.isNetworkAvailable()) {
            true -> {
                interstitialAdManager.fetchAd(context, adUnitId, onAdFetched = {
                    onAdFetched?.invoke()
                })
            }

            false -> {
                Log.d("InterstitialViewModel", "No network available, cannot fetch ad.")
            }
        }
    }

    fun showInterstitialAd(
        activity: Activity,
        onAdDismissed: () -> Unit,
    ) {

        when (canShowAd) {
            true -> {
                Log.d("InterstitialViewModel", "First ad show, pre-fetching ad.")
                canShowAd = false
                interstitialAdManager.showAd(activity, onAdDismissed)
                adTimer.startTimer(30000L, onTick = {
                    Log.d("InterstitialViewModel", "Ad timer tick: $it ms remaining")
                }, onFinish = {
                    canShowAd = true
                })
            }

            false -> {
                Log.d("InterstitialViewModel", "Ad already shown, waiting for timer to finish.")
                // Optionally, you can notify the user that they need to wait
            }

        }

    }

}