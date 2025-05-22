package com.karan.hashin.model.remote

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.karan.hashin.model.local.PassKey

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

    fun addPasskeyToVault(user: FirebaseUser, passKey: PassKey) {
        db.collection(DB_COLLECTION)
            .document(user.uid)
            .collection(VAULT_COLLECTION)
            .add(passKey)
    }
}