package com.karan.hashin.repos

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class AuthRepo {

    companion object {
        private const val DB_COLLECTION: String = "user"
        private const val VAULT_COLLECTION: String = "vault"
        private const val NAME: String = "name"
        private const val EMAIL: String = "email"
        private const val UID: String = "uid"
    }

    private val db = FirebaseFirestore.getInstance()


    fun createUserCollection(user: FirebaseUser) {

        val data = mapOf(
            UID to user.uid,
            EMAIL to user.email,
            NAME to user.displayName
        )
        db.collection(DB_COLLECTION).document(user.uid).set(data)
            .addOnFailureListener {
                Log.e("#ined", "Cannot create user document in 'users' collection", it)
            }
    }
}