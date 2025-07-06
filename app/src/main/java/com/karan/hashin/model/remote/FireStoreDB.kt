package com.karan.hashin.model.remote

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.karan.hashin.model.local.PassKey
import kotlinx.coroutines.tasks.await

class FireStoreDB {

    companion object {
        private const val DB_COLLECTION: String = "users"
        private const val VAULT_COLLECTION: String = "vault"
        private const val NAME: String = "name"
        private const val EMAIL: String = "email"
        private const val UID: String = "uid"
        private const val DT: String = "dateTime"
    }

    private val db = FirebaseFirestore.getInstance()

    suspend fun addPasskeyToVault(user: FirebaseUser, passKey: PassKey) {
        db.collection(DB_COLLECTION)
            .document(user.uid)
            .collection(VAULT_COLLECTION)
            .add(passKey)
    }

    suspend fun getPassKey(user: FirebaseUser): List<PassKey> {
        return try {
            val snapshot = db
                .collection(DB_COLLECTION)
                .document(user.uid)
                .collection(VAULT_COLLECTION)
                .get()
                .await()

            snapshot.documents.mapNotNull {
                it.toObject(PassKey::class.java)
            }
        } catch (e: Exception) {
            Log.e("#ined", "Error fetching passkeys from FireStore Cloud DB", e)
            emptyList()
        }
    }
}