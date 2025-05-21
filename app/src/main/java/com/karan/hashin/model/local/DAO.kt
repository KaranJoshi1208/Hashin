package com.karan.hashin.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPasskey(passKey: PassKey)

    @Query("SELECT * FROM PASSKEY")
    fun getAll(): Flow<List<PassKey>>

    @Delete
    suspend fun delete(passKey: PassKey)
}