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
    suspend fun upsert(passKey: PassKey)

    @Query("SELECT * FROM passkey ORDER BY updatedAt DESC")
    fun getAll(): Flow<List<PassKey>>

    @Query("DELETE FROM passkey WHERE id = :id")
    suspend fun deleteById(id: String)

    @Delete
    suspend fun delete(passKey: PassKey)

    @Query("DELETE FROM passkey")
    suspend fun clearAll()
}