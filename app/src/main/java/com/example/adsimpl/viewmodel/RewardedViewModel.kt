package com.example.adsimpl.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.adsimpl.adManager.RewardedAdManager
import com.example.adsimpl.network.NetworkHandler
import com.google.android.gms.ads.rewarded.RewardItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RewardedViewModel @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val rewardedAdManager: RewardedAdManager
) : ViewModel() {

    fun loadRewardedAd(
        activity: Activity,
        adUnitId: String,
        onLoaded: () -> Unit = {},
        onFailed: (String) -> Unit = {}
    ) {
        when (networkHandler.isNetworkAvailable()) {
            true -> {
                rewardedAdManager.loadRewardedAd(activity, adUnitId, onLoaded, onFailed)
            }

            false -> {
                Log.d("RewardedViewModel", "No network available, cannot load rewarded ad")
                onFailed("No network available")
            }
        }
    }

    fun showRewardedAd(
        activity: Activity,
        onUserEarnedReward: (RewardItem) -> Unit,
        onAdDismissed: () -> Unit = {}
    ) {
        rewardedAdManager.showRewardedAd(activity, onUserEarnedReward, onAdDismissed)
    }

}