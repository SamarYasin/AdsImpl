package com.example.adsimpl.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.adsimpl.adManager.BannerAdManager
import com.example.adsimpl.network.NetworkHandler
import com.google.android.gms.ads.AdView
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BannerViewModel @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val bannerAdManager: BannerAdManager
) : ViewModel() {

    fun loadBannerAd(adView: AdView, onAdLoaded: () -> Unit) {
        when (networkHandler.isNetworkAvailable()) {
            true -> {
                Log.d("BannerViewModel", "Network available, loading banner ad")
                bannerAdManager.loadBannerAd(adView)
                onAdLoaded.invoke()
            }

            false -> {
                Log.d("BannerViewModel", "No network available, cannot load banner ad")
                // Handle no network scenario, e.g., show a placeholder or retry option
            }
        }
    }

    fun showBannerAd(adView: AdView) {
        Log.d("BannerViewModel", "Showing banner ad")
        bannerAdManager.showBannerAd(adView)
    }

    fun hideBannerAd(adView: AdView) {
        Log.d("BannerViewModel", "Hiding banner ad")
        bannerAdManager.hideBannerAd(adView)
    }

}