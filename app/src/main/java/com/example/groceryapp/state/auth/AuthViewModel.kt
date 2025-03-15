package com.example.groceryapp.state.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val auth = Firebase.auth

    private val _authState: MutableStateFlow<FirebaseUser?> = MutableStateFlow(null)
    val authState: StateFlow<FirebaseUser?> = _authState

    init {
        startAuthStateListener()
    }

    private fun startAuthStateListener() {
        auth.addAuthStateListener { _authState.value = it.currentUser }
    }

    suspend fun signIn(email: String, password: String) {
        auth
            .signInWithEmailAndPassword(email, password)
            .await()
    }

    suspend fun createUser(name: String, email: String, password: String) {
        val result = auth
            .createUserWithEmailAndPassword(email, password)
            .await()

        if (result.user != null) {
            result.user?.updateProfile(
                UserProfileChangeRequest
                    .Builder()
                    .setDisplayName(name)
                    .build()
            )
                ?.await()
        }
    }

    fun signOut() {
        auth
            .signOut()
    }
}