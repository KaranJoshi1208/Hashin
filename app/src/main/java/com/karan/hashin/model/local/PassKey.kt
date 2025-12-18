package com.karan.hashin.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.karan.hashin.utils.EncryptedData

@Entity(tableName = "passkey")
data class PassKey(
    @PrimaryKey val id: String = "",
    val service: String = "",
    val userName: String = "",
    val desc: String = "",
    val label: String = "",
    // Only store encrypted password + iv
    val passwordCipher: String = "",
    val passwordIv: String = "",
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        fun fromPlain(
            id: String,
            service: String,
            userName: String,
            password: String,
            desc: String,
            label: String,
            encrypt: (String) -> EncryptedData
        ): PassKey {
            val enc = encrypt(password)
            return PassKey(
                id = id,
                service = service,
                userName = userName,
                desc = desc,
                label = label,
                passwordCipher = enc.cipherText,
                passwordIv = enc.iv,
                updatedAt = System.currentTimeMillis()
            )
        }
    }
}
