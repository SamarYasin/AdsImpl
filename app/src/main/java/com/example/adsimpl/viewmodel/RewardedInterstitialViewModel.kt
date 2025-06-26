package com.example.adsimpl.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.adsimpl.adManager.RewardedInterstitialAdManager
import com.example.adsimpl.network.NetworkHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RewardedInterstitialViewModel @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val rewardedInterstitialAdManager: RewardedInterstitialAdManager
) : ViewModel() {

    fun loadRewardedInterstitialAd(
        context: Context,
        adUnitId: String,
        onAdLoaded: () -> Unit = {},
        onAdFailedToLoad: (String) -> Unit = {}
    ) {
        when (networkHandler.isNetworkAvailable()) {
            true -> {
                rewardedInterstitialAdManager.loadRewardedInterstitialAd(
                    context,
                    adUnitId,
                    onLoaded = onAdLoaded,
                    onFailed = onAdFailedToLoad
                )
            }

            false -> {
                onAdFailedToLoad("No network available")
            }
        }
    }

    fun showRewardedInterstitialAd(
        activity: Activity,
        onUserEarnedReward: () -> Unit = {},
        onAdDismissed: () -> Unit = {}
    ) {
        rewardedInterstitialAdManager.showRewardedInterstitialAd(
            activity = activity,
            onUserEarnedReward = { rewardItem ->
                // Handle the reward given to the user
                Log.d("RewardedInterstitialAd", "User earned reward: ${rewardItem.type} - ${rewardItem.amount}")
                onUserEarnedReward()
            },
            onAdDismissed = {
                // Handle ad dismissal
                onAdDismissed()
            }
        )
    }

}