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
    var passkeys: SnapshotStateList<PassKey> = mutableStateListOf<PassKey>()

    private val user = FirebaseAuth.getInstance().currentUser!!
    private val repo = HomeRepo()
    private val dispatcher = Dispatchers.IO

    init {
        getPassKey(user)
    }

    fun addPassKey(webSite:String, userName: String, pass: String, desc: String, label: String) {
        viewModelScope.launch(dispatcher) {
            val passKey = PassKey(webSite, userName, pass, desc, label)
            repo.addPasskey(user, passKey)
        }
    }

    fun getPassKey(user : FirebaseUser) {
        viewModelScope.launch(dispatcher) {
            isFetchingData = true
            passkeys.addAll(repo.getPassKey(user))
            isFetchingData = false
        }
    }

    fun updatePasskey()
}