package com.example.adsimpl.di

import com.example.adsimpl.adManager.BannerAdManager
import com.example.adsimpl.adManager.InterstitialAdManager
import com.example.adsimpl.adManager.NativeAdManager
import com.example.adsimpl.adManager.RewardedAdManager
import com.example.adsimpl.adManager.RewardedInterstitialAdManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AdModule {
    @Provides
    fun provideNativeAd(): NativeAdManager {
        return NativeAdManager()
    }

    @Provides
    fun provideInterstitialAd(): InterstitialAdManager {
        return InterstitialAdManager()
    }

    @Provides
    fun provideBannerAd(): BannerAdManager {
        return BannerAdManager()
    }

    @Provides
    fun provideRewardedAd(): RewardedAdManager {
        return RewardedAdManager()
    }

    @Provides
    fun provideRewardedInterstitialAd(): RewardedInterstitialAdManager {
        return RewardedInterstitialAdManager()
    }
}