package com.example.adsimpl.adManager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.example.adsimpl.app.AdApplicationClass
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd

class OpenAppAdManager(
    private val applicationClass: AdApplicationClass
) : Application.ActivityLifecycleCallbacks {

    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    private var isShowingAd = false
    private var isAppInBackground = true
    private var activityReferences = 0
    private var isActivityChangingConfigurations = false
    private val adUnitId = "ca-app-pub-3940256099942544/9257395921"

    init {
        applicationClass.registerActivityLifecycleCallbacks(this)
        loadAd()
    }

    private fun loadAd() {
        if (isLoadingAd || appOpenAd != null) return
        isLoadingAd = true
        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            applicationClass, adUnitId, request,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    isLoadingAd = false
                    Log.d("OpenAppAdManager", "Ad loaded")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    appOpenAd = null
                    isLoadingAd = false
                    Log.d("OpenAppAdManager", "Ad failed to load: ${error.message}")
                }
            }
        )
    }

    private fun showAdIfAvailable(activity: Activity) {
        if (isShowingAd || appOpenAd == null) {
            Log.d("OpenAppAdManager", "Ad not available or already showing")
            loadAd()
            return
        }
        isShowingAd = true
        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                appOpenAd = null
                isShowingAd = false
                loadAd()
                Log.d("OpenAppAdManager", "Ad dismissed")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                appOpenAd = null
                isShowingAd = false
                loadAd()
                Log.d("OpenAppAdManager", "Ad failed to show: ${adError.message}")
            }
        }
        appOpenAd?.show(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        if (isAppInBackground) {
            isAppInBackground = false
            showAdIfAvailable(activity)
            Log.d("OpenAppAdManager", "App resumed from background, showing ad if available")
        }
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStarted(activity: Activity) {
        if (++activityReferences == 1 && !isActivityChangingConfigurations) {
            Log.d("OpenAppAdManager", "App entered foreground, activity references: $activityReferences")
        }
    }

    override fun onActivityStopped(activity: Activity) {
        isActivityChangingConfigurations = activity.isChangingConfigurations
        if (--activityReferences == 0 && !isActivityChangingConfigurations) {
            isAppInBackground = true
            Log.d("OpenAppAdManager", "App entered background, activity references: $activityReferences")
        }
    }

    override fun onActivityCreated(a: Activity, b: Bundle?) {}
    override fun onActivitySaveInstanceState(a: Activity, b: Bundle) {}
    override fun onActivityDestroyed(a: Activity) {}

}