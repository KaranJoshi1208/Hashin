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
import com.google.firebase.firestore.FirebaseFirestore
import com.karan.hashin.repos.AuthRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    companion object {
        private const val CLIENT_ID =
            "1054238295659-pl325m0625td0g2vcgm87gbrak9rs412.apps.googleusercontent.com"
    }


    private val authRepo : AuthRepo = AuthRepo()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _isAuthenticated = MutableStateFlow(auth.currentUser != null)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _isAuthenticated.value = firebaseAuth.currentUser != null
        }
    }

    fun signIn(email: String, pass: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener() {
                _isAuthenticated.value = true
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
                it.user?.apply {
                    updateProfile(
                        UserProfileChangeRequest
                            .Builder()
                            .setDisplayName(userName)
                            .build()
                    )
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                _isAuthenticated.value = true
                                authRepo.createUserCollection(this@apply)
                                onSuccess()
                            } else {
                                onFailure(
                                    task.exception ?: Exception("Unknown error during profile update")
                                )
                            }
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
            if (idToken == null) return Result.failure(Exception("Google ID token is null"))
            val fbCredential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = FirebaseAuth.getInstance()
                .signInWithCredential(fbCredential)
                .await()
            _isAuthenticated.value = true

            authResult.user?.let { user ->
                val userDocRef = FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(user.uid)

                val docSnapshot = userDocRef.get().await()
                if (!docSnapshot.exists()) {
                    authRepo.createUserCollection(user)
                }
            } ?: return Result.failure(Exception("Authenticated user is null"))
            Result.success(authResult.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
        _isAuthenticated.value = false
    }
}