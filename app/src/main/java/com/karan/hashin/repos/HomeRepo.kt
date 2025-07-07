package com.karan.hashin.repos

import com.google.firebase.auth.FirebaseUser
import com.karan.hashin.model.local.PassKey
import com.karan.hashin.model.remote.FireStoreDB

class HomeRepo {

    val fire: FireStoreDB = FireStoreDB()

    suspend fun addPasskey(user: FirebaseUser, passKey: PassKey) {
        fire.addPasskeyToVault(user, passKey)
    }

    suspend fun getPasskey(user : FirebaseUser): List<PassKey> {
        return fire.getPasskey(user)
    }

    suspend fun updatePasskey(user: FirebaseUser, newPassKey: PassKey): Boolean {
        return fire.updatePasskey(user, newPassKey)
    }
}