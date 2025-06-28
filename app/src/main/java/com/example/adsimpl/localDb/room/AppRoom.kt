package com.example.adsimpl.localDb.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AppEntity::class], version = 1, exportSchema = false)
abstract class AppRoom : RoomDatabase() {

    abstract fun getDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoom? = null

        fun getInstance(context: Context): AppRoom {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppRoom {
            return Room.databaseBuilder(
                context.applicationContext,
                AppRoom::class.java,
                "app_database"
            ).build()
        }
    }

}