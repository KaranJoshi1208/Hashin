package com.karan.hashin.viewmodel

import androidx.compose.runtime.mutableStateListOf
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
    // this defines the index of the passkey that user tapped on
    var userSelected = -1
    var passkeys: SnapshotStateList<PassKey> = mutableStateListOf<PassKey>()

    private val user = FirebaseAuth.getInstance().currentUser!!
    private val repo = HomeRepo()
    private val dispatcher = Dispatchers.IO

    init {
        getPassKey(user)
    }

    fun addPassKey(service: String, userName: String, pass: String, desc: String, label: String) {
        viewModelScope.launch(dispatcher) {
            val passKey = PassKey(
                id = "",
                service = service,
                userName = userName,
                pass = pass,
                desc = desc,
                label = label)
            repo.addPasskey(user, passKey)
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
            repo.updatePasskey(user, newPassKey)
        }
    }
}