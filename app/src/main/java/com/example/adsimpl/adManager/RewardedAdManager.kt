package com.example.adsimpl.adManager

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import javax.inject.Inject

class RewardedAdManager @Inject constructor() {

    private var rewardedAd: RewardedAd? = null

    fun loadRewardedAd(
        activity: Activity,
        adUnitId: String,
        onLoaded: () -> Unit = {},
        onFailed: (String) -> Unit = {}
    ) {
        Log.d("RewardedAdManager", "Loading rewarded ad with unit ID: $adUnitId")
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(activity, adUnitId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdLoaded(ad: RewardedAd) {
                rewardedAd = ad
                onLoaded()
                Log.d("RewardedAdManager", "Rewarded ad loaded successfully")
            }

            override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                rewardedAd = null
                onFailed(error.message)
                Log.e("RewardedAdManager", "Failed to load rewarded ad: ${error.message}")
            }
        })
    }

    fun showRewardedAd(
        activity: Activity,
        onUserEarnedReward: (RewardItem) -> Unit,
        onAdDismissed: () -> Unit = {}
    ) {
        Log.d("RewardedAdManager", "Showing rewarded ad")
        rewardedAd?.show(activity) { rewardItem ->
            onUserEarnedReward(rewardItem)
        }
        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null
                onAdDismissed()
                Log.d("RewardedAdManager", "Rewarded ad dismissed")
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                rewardedAd = null
                onAdDismissed()
                Log.e("RewardedAdManager", "Failed to show rewarded ad: ${error.message}")
            }
        }
    }

}