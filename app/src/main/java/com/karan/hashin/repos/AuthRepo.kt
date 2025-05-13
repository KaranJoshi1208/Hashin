package com.karan.hashin.repos

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
        )

        db.collection(DB_COLLECTION).document(user.uid)
    }
}