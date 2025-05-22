package com.karan.hashin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.karan.hashin.model.local.PassKey
import com.karan.hashin.repos.HomeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val user = FirebaseAuth.getInstance().currentUser!!
    private val repo = HomeRepo()
    private val dispatcher = Dispatchers.IO


    fun addPassKey(userName: String, pass: String, desc: String, label: String) {
        viewModelScope.launch(dispatcher) {
            val passKey = PassKey(userName, pass, desc, label)
            repo.addPasskey(user, passKey)
        }
    }


}