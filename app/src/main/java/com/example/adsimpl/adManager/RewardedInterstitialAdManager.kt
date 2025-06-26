package com.example.adsimpl.adManager

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import javax.inject.Inject

class RewardedInterstitialAdManager @Inject constructor() {

    private var rewardedInterstitialAd: RewardedInterstitialAd? = null

    fun loadRewardedInterstitialAd(
        context: Context,
        adUnitId: String,
        onLoaded: () -> Unit = {},
        onFailed: (String) -> Unit = {}
    ) {
        RewardedInterstitialAd.load(
            context,
            adUnitId,
            AdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    rewardedInterstitialAd = ad
                    Log.d("RewardedInterstitialAd", "Ad was loaded.")
                    onLoaded()
                }

                override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                    rewardedInterstitialAd = null
                    Log.e("RewardedInterstitialAd", "Ad failed to load: ${error.message}")
                    onFailed(error.message ?: "Unknown error")
                }
            })
    }

    fun showRewardedInterstitialAd(
        activity: Activity,
        onUserEarnedReward: (RewardItem) -> Unit,
        onAdDismissed: () -> Unit = {}
    ) {
        rewardedInterstitialAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    rewardedInterstitialAd = null
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    rewardedInterstitialAd = null
                    onAdDismissed()
                }
            }
        rewardedInterstitialAd?.show(activity) { rewardItem ->
            onUserEarnedReward(rewardItem)
        }
    }

}