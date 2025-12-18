package com.karan.hashin.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.karan.hashin.model.local.Database
import com.karan.hashin.model.local.PassKey
import com.karan.hashin.repos.HomeRepo
import com.karan.hashin.utils.AppContextHolder
import com.karan.hashin.utils.CryptoManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class HomeViewModel : ViewModel() {

    var isFetchingData by mutableStateOf(false)

    // passkey screen flags
    var processing by mutableStateOf(false)

    // this defines the index of the passkey that user tapped on
    var userSelected = -1

    private val _passkeysFlow = MutableStateFlow<List<PassKey>>(emptyList())
    val passkeysFlow: StateFlow<List<PassKey>> = _passkeysFlow

    // data
    var passkeys: SnapshotStateList<PassKey> = mutableStateListOf<PassKey>()

    // utilities
    private val user get() = FirebaseAuth.getInstance().currentUser
    private val crypto = CryptoManager()
    private val repo by lazy {
        val dao = Database.getDatabase(AppContextHolder.appContext).dao()
        HomeRepo(dao)
    }
    private val dispatcher = Dispatchers.IO

    init {
        observeLocal()
        refreshFromRemote()
    }

    private suspend fun replacePasskeysIfChanged(newList: List<PassKey>) {
        if (passkeys.size == newList.size && passkeys.zip(newList).all { (a, b) -> a.id == b.id && a.updatedAt == b.updatedAt }) {
            return
        }
        passkeys.clear()
        passkeys.addAll(newList)
        _passkeysFlow.update { newList }
    }

    private fun observeLocal() {
        viewModelScope.launch(dispatcher) {
            repo.observeLocal().collectLatest { list ->
                withContext(Dispatchers.Main) {
                    replacePasskeysIfChanged(list)
                }
            }
        }
    }

    fun detectChange(vararg pairs: Pair<String, String>): Boolean {
        return pairs.any { it.first != it.second }
    }

    fun asyncTask(task: () -> Unit) {
        viewModelScope.launch(dispatcher){ task() }
    }

    fun addPassKey(service: String, username: String, pass: String, desc: String, label: String) {
        val currentUser = user ?: return
        viewModelScope.launch(dispatcher) {
            processing = true
            val entity = PassKey.fromPlain(
                id = UUID.randomUUID().toString(),
                service = service,
                userName = username,
                password = pass,
                desc = desc,
                label = label,
                encrypt = crypto::encrypt
            )
            repo.upsertLocal(entity)
            repo.addRemote(currentUser, entity)
            withContext(Dispatchers.Main) { processing = false }
        }
    }

    fun refreshFromRemote() {
        val currentUser = user ?: return
        if (isFetchingData) return
        viewModelScope.launch(dispatcher) {
            isFetchingData = true
            val remote = repo.syncRemote(currentUser)
            repo.clearLocal()
            remote.forEach { repo.upsertLocal(it) }
            withContext(Dispatchers.Main) { isFetchingData = false }
        }
    }

    fun updatePasskey(plainPassword: String?, newPassKey: PassKey) {
        val currentUser = user ?: return
        viewModelScope.launch(dispatcher) {
            processing = true
            val updatedEntity = if (plainPassword != null) {
                PassKey.fromPlain(
                    id = newPassKey.id,
                    service = newPassKey.service,
                    userName = newPassKey.userName,
                    password = plainPassword,
                    desc = newPassKey.desc,
                    label = newPassKey.label,
                    encrypt = crypto::encrypt
                )
            } else {
                newPassKey.copy(updatedAt = System.currentTimeMillis())
            }
            repo.upsertLocal(updatedEntity)
            repo.updateRemote(currentUser, updatedEntity)
            withContext(Dispatchers.Main) {
                val idx = passkeys.indexOfFirst { it.id == updatedEntity.id }
                if (idx != -1) passkeys[idx] = updatedEntity
                processing = false
            }
        }
    }

    fun deletePasskey(id: String) {
        val currentUser = user ?: return
        viewModelScope.launch(dispatcher) {
            repo.deleteLocal(id)
            repo.deleteRemote(currentUser, id)
            withContext(Dispatchers.Main) { userSelected = -1 }
        }
    }

    fun decryptPassword(passKey: PassKey): String {
        return crypto.decrypt(
            com.karan.hashin.utils.EncryptedData(
                cipherText = passKey.passwordCipher,
                iv = passKey.passwordIv
            )
        )
    }

    fun clearLocalData() {
        viewModelScope.launch(dispatcher) {
            repo.clearLocal()
            withContext(Dispatchers.Main) {
                passkeys.clear()
                userSelected = -1
            }
        }
    }

    fun onUserChanged() {
        val currentUser = user
        viewModelScope.launch(dispatcher) {
            repo.clearLocal()
            withContext(Dispatchers.Main) {
                passkeys.clear()
                userSelected = -1
            }
            if (currentUser != null) {
                val remote = repo.syncRemote(currentUser)
                remote.forEach { repo.upsertLocal(it) }
            }
        }
    }

    fun deleteAccount(onResult: (Boolean, String?) -> Unit) {
        val currentUser = user ?: return onResult(false, "Not signed in")
        viewModelScope.launch(dispatcher) {
            try {
                repo.deleteUser(currentUser)
                repo.clearLocal()
                FirebaseAuth.getInstance().currentUser?.delete()?.await()
                withContext(Dispatchers.Main) {
                    passkeys.clear()
                    userSelected = -1
                    onResult(true, null)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onResult(false, e.message) }
            }
        }
    }
}