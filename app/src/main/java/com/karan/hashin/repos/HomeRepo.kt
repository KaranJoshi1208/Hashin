package com.karan.hashin.repos

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.karan.hashin.model.local.PassKey
import com.karan.hashin.model.remote.FireStoreDB

class HomeRepo {

    val fire: FireStoreDB = FireStoreDB()

    suspend fun addPasskey(user: FirebaseUser, passKey: PassKey) {
        fire.addPasskeyToVault(user, passKey)
    }

    suspend fun getPassKey(user : FirebaseUser): List<PassKey> {
        return fire.getPassKey(user)
    }
}