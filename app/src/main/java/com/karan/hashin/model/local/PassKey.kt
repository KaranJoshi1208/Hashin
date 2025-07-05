package com.karan.hashin.model.local

import androidx.room.Entity


@Entity(tableName = "passkey")
data class PassKey(
    val webSite: String = "",
    val userName: String = "",
    val pass: String = "",
    val desc: String = "",
    val label: String = ""
)