package com.example.adsimpl.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adsimpl.adManager.NativeAdManager
import com.example.adsimpl.network.NetworkHandler
import com.google.android.gms.ads.nativead.NativeAdView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NativeViewModel @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val nativeAdManager: NativeAdManager
) : ViewModel() {

    fun preFetchNativeAd(
        context: Context,
        adUnitId: String,
        onAdFetched: (() -> Unit)? = null
    ) {
        when (networkHandler.isNetworkAvailable()) {
            true -> {
                Log.d("NativeViewModel", "Network is available, fetching ad for $adUnitId")
                nativeAdManager.fetchAd(context, adUnitId, onAdFetched = {
                    Log.d("NativeViewModel", "Ad fetched successfully for $adUnitId")
                    onAdFetched?.invoke()
                })
            }

            false -> {
                Log.d("NativeViewModel", "Network is not available, skipping ad fetch")
            }
        }
    }

    fun showNativeAd(
        context: Context,
        adUnitId: String,
        adView: NativeAdView,
        shimmerLayout: View? = null,
        onAdShown: (() -> Unit)? = null
    ) {
        nativeAdManager.showAd(
            context = context,
            adUnitId = adUnitId,
            adView = adView,
            shimmerLayout = shimmerLayout,
            onAdShown = onAdShown
        )
    }

    fun clearAdCache() {
        viewModelScope.launch {
            Log.d("NativeViewModel", "Clearing ad cache")
            nativeAdManager.destroy()
        }
    }

}