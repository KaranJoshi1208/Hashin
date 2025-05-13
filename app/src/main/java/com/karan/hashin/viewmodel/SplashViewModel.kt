package com.karan.hashin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.karan.hashin.navigation.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    var to : String
    var auth : FirebaseAuth = FirebaseAuth.getInstance()

    init {
        to = if (auth.currentUser != null) Screens.Home.route else Screens.Auth.route
    }

    fun move(action : () -> Unit) = viewModelScope.launch {
        delay(3500)
        action()
    }
}