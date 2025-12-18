package com.karan.hashin.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * AES/GCM helper backed by Android Keystore (per-device key).
 */
class CryptoManager {
    private val keyAlias = "hashin_passkey_key"
    private val androidKeyStore = "AndroidKeyStore"
    private val transformation = "AES/GCM/NoPadding"

    fun encrypt(plainText: String): EncryptedData {
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
        val cipherBytes = cipher.doFinal(plainText.toByteArray())
        val iv = cipher.iv
        return EncryptedData(
            cipherText = Base64.encodeToString(cipherBytes, Base64.NO_WRAP),
            iv = Base64.encodeToString(iv, Base64.NO_WRAP)
        )
    }

    fun decrypt(data: EncryptedData): String {
        val cipher = Cipher.getInstance(transformation)
        val ivBytes = Base64.decode(data.iv, Base64.NO_WRAP)
        val spec = GCMParameterSpec(128, ivBytes)
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateKey(), spec)
        val plainBytes = cipher.doFinal(Base64.decode(data.cipherText, Base64.NO_WRAP))
        return String(plainBytes)
    }

    private fun getOrCreateKey(): SecretKey {
        val keyStore = KeyStore.getInstance(androidKeyStore).apply { load(null) }
        val existingKey = (keyStore.getKey(keyAlias, null) as? SecretKey)
        if (existingKey != null) return existingKey

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, androidKeyStore)
        val parameterSpec = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        keyGenerator.init(parameterSpec)
        return keyGenerator.generateKey()
    }
}

/** Encapsulates cipher text and IV (Base64 encoded). */
data class EncryptedData(
    val cipherText: String = "",
    val iv: String = ""
)

