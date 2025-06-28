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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdModule {

    @Provides
    @Singleton
    fun provideNativeAd(): NativeAdManager {
        return NativeAdManager()
    }

    @Provides
    @Singleton
    fun provideInterstitialAd(): InterstitialAdManager {
        return InterstitialAdManager()
    }

    @Provides
    @Singleton
    fun provideBannerAd(): BannerAdManager {
        return BannerAdManager()
    }

    @Provides
    @Singleton
    fun provideRewardedAd(): RewardedAdManager {
        return RewardedAdManager()
    }

    @Provides
    @Singleton
    fun provideRewardedInterstitialAd(): RewardedInterstitialAdManager {
        return RewardedInterstitialAdManager()
    }

}