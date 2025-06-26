package com.example.adsimpl.di

import com.example.adsimpl.adManager.AdTimer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TimerModule {
    @Provides
    fun provideAdTimer(): AdTimer {
        return AdTimer()
    }
}