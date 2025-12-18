package com.karan.hashin.repos

import com.google.firebase.auth.FirebaseUser
import com.karan.hashin.model.local.DAO
import com.karan.hashin.model.local.PassKey
import com.karan.hashin.model.remote.FireStoreDB
import kotlinx.coroutines.flow.Flow

class HomeRepo(private val dao: DAO, private val remote: FireStoreDB = FireStoreDB()) {

    fun observeLocal(): Flow<List<PassKey>> = dao.getAll()

    suspend fun upsertLocal(passKey: PassKey) = dao.upsert(passKey)

    suspend fun deleteLocal(id: String) = dao.deleteById(id)

    suspend fun syncRemote(user: FirebaseUser): List<PassKey> = remote.getPasskey(user)

    suspend fun addRemote(user: FirebaseUser, passKey: PassKey) = remote.addPasskeyToVault(user, passKey)

    suspend fun updateRemote(user: FirebaseUser, newPassKey: PassKey): Boolean = remote.updatePasskey(user, newPassKey)

    suspend fun deleteRemote(user: FirebaseUser, id: String): Boolean = remote.deletePasskey(user, id)

    suspend fun clearLocal() = dao.clearAll()

    suspend fun deleteUser(user: FirebaseUser) = remote.deleteUserData(user)
}