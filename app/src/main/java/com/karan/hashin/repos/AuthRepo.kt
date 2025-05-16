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

    fun createUserCollection(user: FirebaseUser) {

        Log.d("#ined", "Creating user UID document.....")
        val data = mapOf(
            NAME to user.displayName,
            EMAIL to user.email,
            UID to user.uid,
            DT to (FieldValue.serverTimestamp())
        )
        db.collection(DB_COLLECTION).document(user.uid).set(data)
            .addOnSuccessListener {
                Log.d("#ined", "Successfully created UID doc ✔️")

                // Creation of sub-collection "vault"
                db.collection(DB_COLLECTION)
                    .document(user.uid)
                    .collection(VAULT_COLLECTION)
            }
            .addOnFailureListener {
                Log.e("#ined", "Cannot create user document in 'users' collection ❌", it)
            }
    }
}