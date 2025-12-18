package com.karan.hashin.repos

import com.google.firebase.firestore.FieldValue
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class AuthRepo {

    companion object {
        private const val DB_COLLECTION: String = "users"
        private const val VAULT_COLLECTION: String = "vault"
        private const val NAME: String = "name"
        private const val EMAIL: String = "email"
        private const val UID: String = "uid"
        private const val DT: String = "dateTime"
    }

    private val db = FirebaseFirestore.getInstance()

    fun createUserDocument(uid: String, name: String?, email: String?) {
        Log.d("#ined", "Creating user document for UID=$uid ...")
        val data = mapOf(
            NAME to (name ?: ""),
            EMAIL to (email ?: ""),
            UID to uid,
            DT to FieldValue.serverTimestamp()
        )
        db.collection(DB_COLLECTION).document(uid).set(data)
            .addOnSuccessListener {
                Log.d("#ined", "Successfully created UID doc ✔️")
            }
            .addOnFailureListener {
                Log.e("#ined", "Cannot create user document in 'users' collection ❌", it)
            }
    }
}