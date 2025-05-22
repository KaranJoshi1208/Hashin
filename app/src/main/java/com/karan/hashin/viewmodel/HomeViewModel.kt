package com.karan.hashin.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.karan.hashin.model.local.PassKey
import com.karan.hashin.navigation.Screens
import com.karan.hashin.repos.HomeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var passkeys: SnapshotStateList<PassKey> = mutableStateListOf<PassKey>()

    private val user = FirebaseAuth.getInstance().currentUser!!
    private val repo = HomeRepo()
    private val dispatcher = Dispatchers.IO

    init {
        getPassKey(user)
    }


    fun addPassKey(userName: String, pass: String, desc: String, label: String) {
        viewModelScope.launch(dispatcher) {
            val passKey = PassKey(userName, pass, desc, label)
            repo.addPasskey(user, passKey)
        }
    }

    fun getPassKey(user : FirebaseUser) {
        viewModelScope.launch(dispatcher) {
            passkeys.addAll(repo.getPassKey(user))
        }
    }


}