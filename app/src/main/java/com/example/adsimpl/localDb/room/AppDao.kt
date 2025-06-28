package com.example.adsimpl.localDb.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AppDao {

     @Insert
     suspend fun insert(entity: AppEntity)

     @Query("SELECT * FROM AppEntity WHERE id = :id")
     suspend fun getById(id: Int): AppEntity?

     @Update
     suspend fun update(entity: AppEntity)

     @Delete
     suspend fun delete(entity: AppEntity)

}