package com.karan.hashin.model.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase


@androidx.room.Database(entities = [PassKey::class], version = 1, exportSchema = true)
abstract class Database : RoomDatabase() {

    abstract fun dao(): DAO

    companion object {
        @Volatile private var INSTANCE: Database? = null

        fun getDatabase(context: Context): Database {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, Database::class.java, "app_database")
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}