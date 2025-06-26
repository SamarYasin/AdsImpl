package com.example.adsimpl.adManager

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.adsimpl.R
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import javax.inject.Inject

class NativeAdManager @Inject constructor() {

    private var cachedNativeAd: NativeAd? = null

    // Fetch an ad and cache it
    fun fetchAd(context: Context, adUnitId: String, onAdFetched: (() -> Unit)? = null) {
        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad ->
                Log.d("NativeAdManager", "Native ad fetched: ${ad.headline}")
                cachedNativeAd?.destroy()
                cachedNativeAd = ad
                onAdFetched?.invoke()
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("NativeAdManager", "Fetch failed: ${error.message}")
                    cachedNativeAd?.destroy()
                    cachedNativeAd = null
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    // Load (render) the cached ad into a view
    private fun loadAd(
        adView: NativeAdView,
        shimmerLayout: View? = null,
        onAdLoaded: (() -> Unit)? = null,
        onAdNotAvailable: (() -> Unit)? = null
    ) {
        Log.d("NativeAdManager", "Loading cached ad into view")
        shimmerLayout?.visibility = View.VISIBLE

        val nativeAd = cachedNativeAd
        if (nativeAd != null) {
            shimmerLayout?.visibility = View.GONE
            populateNativeAdView(nativeAd, adView)
            onAdLoaded?.invoke()
        } else {
            shimmerLayout?.visibility = View.GONE
            onAdNotAvailable?.invoke()
            Log.w("NativeAdManager", "No cached ad to load")
        }
    }

    // Show cached ad and immediately fetch a new one
    fun showAd(
        context: Context,
        adUnitId: String,
        adView: NativeAdView,
        shimmerLayout: View? = null,
        onAdShown: (() -> Unit)? = null
    ) {
        loadAd(
            adView = adView,
            shimmerLayout = shimmerLayout,
            onAdLoaded = {
                onAdShown?.invoke()
                fetchAd(context, adUnitId)
            },
            onAdNotAvailable = {
                Log.d("NativeAdManager", "Ad not shown as none was cached")
                fetchAd(context, adUnitId)
            }
        )
    }

    // Destroys any cached ad manually (optional)
    fun destroy() {
        cachedNativeAd?.destroy()
        cachedNativeAd = null
    }

    // Binds NativeAd data to the view
    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        Log.d("NativeAdManager", "Populating NativeAdView with ad data")
        adView.headlineView = adView.findViewById(R.id.ad_title)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.iconView = adView.findViewById(R.id.ad_icon)
        adView.mediaView = adView.findViewById(R.id.ad_media)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)

        (adView.headlineView as? TextView)?.text = nativeAd.headline
        (adView.bodyView as? TextView)?.text = nativeAd.body
        (adView.iconView as? ImageView)?.setImageDrawable(nativeAd.icon?.drawable)
        (adView.callToActionView as? Button)?.text = nativeAd.callToAction

        adView.setNativeAd(nativeAd)
    }
}