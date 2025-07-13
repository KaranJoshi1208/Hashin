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

class HomeViewModel : ViewModel() {

    var isFetchingData = false

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


    fun addPassKey(service: String, username: String, pass: String, desc: String, label: String) {
        viewModelScope.launch(dispatcher) {
            processing = true
            val passKey = PassKey(
                id = "",
                service = service,
                userName = username,
                pass = pass,
                desc = desc,
                label = label)
            repo.addPasskey(user, passKey)
            processing = false
        }
    }

    fun getPassKey(user : FirebaseUser) {
        viewModelScope.launch(dispatcher) {
            isFetchingData = true
            passkeys.addAll(repo.getPasskey(user))
            isFetchingData = false
        }
    }

    fun updatePasskey(newPassKey: PassKey) {
        viewModelScope.launch(dispatcher) {
            processing = true
            repo.updatePasskey(user, newPassKey)
            processing = false
        }
    }

    fun deletePasskey(id: String) {
        viewModelScope.launch(dispatcher) {
            repo.deletePasskey(user, id)
        }
    }
}