package com.karan.hashin.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class AuthViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()


    fun signIn(email : String, pass : String, onSuccess : () -> Unit, onFailure : (Exception) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener() {
                onSuccess()
            }
            .addOnFailureListener() {
                onFailure(it)
            }
    }

    fun signUp(userName : String, email : String, pass : String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                it.user?.updateProfile(
                    UserProfileChangeRequest
                        .Builder()
                        .setDisplayName(userName)
                        .build()
                )
                    ?.addOnCompleteListener() { task ->
                        if(task.isSuccessful) {
                            onSuccess()
                        } else {
                            onFailure(task.exception ?: Exception("Unknown error during profile update"))
                        }
                    }
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }
}