package com.karan.hashin.model.local

import androidx.room.Entity


@Entity(tableName = "passkey")
data class PassKey(
    var id : String = "",
    var service: String = "",
    var userName: String = "",
    var pass: String = "",
    var desc: String = "",
    var label: String = ""
)