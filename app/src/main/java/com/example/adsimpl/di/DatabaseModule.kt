package com.example.adsimpl.di

import android.content.Context
import androidx.room.Room
import com.example.adsimpl.localDb.room.AppDao
import com.example.adsimpl.localDb.room.AppRoom
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppRoom =
        Room.databaseBuilder(context, AppRoom::class.java, "app_database").build()

    @Provides
    fun provideAppDao(db: AppRoom): AppDao = db.getDao()

}