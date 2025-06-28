package com.example.adsimpl.localDb.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AppEntity")
data class AppEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val description: String
)
