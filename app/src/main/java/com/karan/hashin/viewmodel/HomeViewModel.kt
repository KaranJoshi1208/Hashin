package com.karan.hashin.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.karan.hashin.model.local.PassKey
import com.karan.hashin.repos.HomeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    var isFetchingData by mutableStateOf(false)

    // passkey screen flags
    var processing by mutableStateOf(false)

    // this defines the index of the passkey that user tapped on
    var userSelected = -1

    // data
    var passkeys: SnapshotStateList<PassKey> = mutableStateListOf<PassKey>()


    // utilities
    private val user = FirebaseAuth.getInstance().currentUser!!
    private val repo = HomeRepo()
    private val dispatcher = Dispatchers.IO

    init {
        getPassKey(user)
    }

    fun detectChange(vararg pairs: Pair<String, String>): Boolean {
        return pairs.any { it.first != it.second }
    }

    fun asyncTask(task: () -> Unit) {
        viewModelScope.launch(dispatcher){
            task()
        }
    }


    fun addPassKey(service: String, username: String, pass: String, desc: String, label: String) {
        viewModelScope.launch(dispatcher) {
            processing = true
            val passKey = PassKey(
                id = "",
                service = service,
                userName = username,
                pass = pass,
                desc = desc,
                label = label
            )
            repo.addPasskey(user, passKey)
            val refreshed = repo.getPasskey(user)
            withContext(Dispatchers.Main) {
                passkeys.clear()
                passkeys.addAll(refreshed)
                processing = false
            }
        }
    }

    fun getPassKey(user: FirebaseUser) {
        viewModelScope.launch(dispatcher) {
            isFetchingData = true
            val refreshed = repo.getPasskey(user)
            withContext(Dispatchers.Main) {
                passkeys.clear()
                passkeys.addAll(refreshed)
                isFetchingData = false
            }
        }
    }

    fun updatePasskey(newPassKey: PassKey) {
        viewModelScope.launch(dispatcher) {
            processing = true
            val success = repo.updatePasskey(user, newPassKey)
            if (success) {
                withContext(Dispatchers.Main) {
                    val idx = passkeys.indexOfFirst { it.id == newPassKey.id }
                    if (idx != -1) passkeys[idx] = newPassKey
                    processing = false
                }
            } else {
                withContext(Dispatchers.Main) { processing = false }
            }
        }
    }

    fun deletePasskey(id: String) {
        viewModelScope.launch(dispatcher) {
            val success = repo.deletePasskey(user, id)
            if (success) {
                withContext(Dispatchers.Main) {
                    passkeys.removeAll { it.id == id }
                    userSelected = -1
                }
            }
        }
    }
}