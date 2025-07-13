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
        val doc = db.collection(DB_COLLECTION)
            .document(user.uid)
            .collection(VAULT_COLLECTION)
            .document()
        passKey.id = doc.id
        doc.set(passKey).await()
    }

    suspend fun getPasskey(user: FirebaseUser): List<PassKey> {
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

    suspend fun updatePasskey(user: FirebaseUser, newPassKey: PassKey): Boolean {
        return try {
            db.collection(DB_COLLECTION)
                .document(user.uid)
                .collection(VAULT_COLLECTION)
                .document(newPassKey.id)
                .set(newPassKey)   // Overwrites the document
                .await()
            true
        } catch (e: Exception) {
            Log.e("#ined", "Error updating passkey", e)
            false
        }
    }

    suspend fun deletePasskey(user: FirebaseUser, id: String) : Boolean {
        return try {
            db.collection(DB_COLLECTION)
                .document(user.uid)
                .collection(VAULT_COLLECTION)
                .document(id)
                .delete()
                .await()
            true
        } catch(e : Exception) {
            Log.e("#ined", "Error deleting passkey", e)
            false
        }
    }
}