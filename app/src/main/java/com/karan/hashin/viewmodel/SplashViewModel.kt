package com.karan.hashin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.karan.hashin.navigation.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    var auth : FirebaseAuth = FirebaseAuth.getInstance()

    fun move(action : () -> Unit) = viewModelScope.launch {
        delay(3500)
        action()
    }
}