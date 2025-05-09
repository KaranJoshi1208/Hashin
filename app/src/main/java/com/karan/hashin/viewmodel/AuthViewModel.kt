package com.karan.hashin.viewmodel

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    companion object {
        private const val CLIENT_ID =
            "1054238295659-pl325m0625td0g2vcgm87gbrak9rs412.apps.googleusercontent.com"
    }

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    fun signIn(email: String, pass: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener() {
                onSuccess()
            }
            .addOnFailureListener() {
                onFailure(it)
            }
    }

    fun signUp(
        userName: String,
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                it.user?.updateProfile(
                    UserProfileChangeRequest
                        .Builder()
                        .setDisplayName(userName)
                        .build()
                )
                    ?.addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            onSuccess()
                        } else {
                            onFailure(
                                task.exception ?: Exception("Unknown error during profile update")
                            )
                        }
                    }
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }

    suspend fun googleCredentialAuth(
        context: Context,
    ): Result<FirebaseUser?> {

        return try {
            val cm = CredentialManager.create(context)
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(CLIENT_ID)
                .build()
            val request = GetCredentialRequest(listOf(googleIdOption))
            val result = cm.getCredential(
                request = request,
                context = context
            )
            val credential = result.credential as? GoogleIdTokenCredential
            val idToken = credential?.idToken
            if (idToken == null) return Result.failure(Exception("Google ID token is fucked 💀"))
            val fbCredential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = FirebaseAuth.getInstance()
                .signInWithCredential(fbCredential)
                .await()
            Result.success(authResult.user)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}