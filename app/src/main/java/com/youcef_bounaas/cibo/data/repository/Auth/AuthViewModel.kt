package com.youcef_bounaas.cibo.data.repository.Auth


import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youcef_bounaas.cibo.data.model.SessionManager
import com.youcef_bounaas.cibo.data.repository.SupabaseClientProvider
import com.youcef_bounaas.cibo.features.Authentication.signIn
import com.youcef_bounaas.cibo.features.Authentication.signUp
import kotlinx.coroutines.launch
import io.github.jan.supabase.auth.auth
import android.util.Log




import androidx.lifecycle.SavedStateHandle


class AuthViewModel(
    @SuppressLint("StaticFieldLeak") private val context: Context,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val sessionManager = SessionManager.getInstance(context)
    private val supabase = SupabaseClientProvider.supabase
    var authState = mutableStateOf("")

    init {
        viewModelScope.launch {
            checkCurrentSession()
        }
    }

    private suspend fun checkCurrentSession() {
        try {
            val session = supabase.auth.currentSessionOrNull()
            if (session != null) {
                Log.d("AuthViewModel", "Existing session found: ${session.user?.email}")
                sessionManager.updateSession(
                    isLoggedIn = true,
                    email = session.user?.email ?: "",
                    userId = session.user?.id ?: ""
                )
                authState.value = "Sign in successful: ${session.user?.email}"
            } else {
                Log.d("AuthViewModel", "No active session found")
                authState.value = "No active session"
            }
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Error during session check: ${e.message}")
            authState.value = "Error checking session"
        }
    }

    fun signUpUser(email: String, password: String, name: String, role: String) {
        Log.d("AuthViewModel", "Starting sign up with email: $email")
        viewModelScope.launch {
            val result = signUp(email, password, name, role)
            Log.d("AuthViewModel", "Sign up result: $result")
            authState.value = result
            if (result.startsWith("Signup successful")) {
                try {
                    val session = supabase.auth.currentSessionOrNull()
                    Log.d("AuthViewModel", "Updating session after sign up: $email")
                    sessionManager.updateSession(
                        isLoggedIn = true,
                        email = email,
                        userId = session?.user?.id ?: ""
                    )
                } catch (e: Exception) {
                    Log.e("AuthViewModel", "Error saving session after sign up: ${e.message}")
                }
            }
        }
    }

    fun signInUser(email: String, password: String) {
        Log.d("AuthViewModel", "Starting sign in with email: $email")
        viewModelScope.launch {
            val result = signIn(email, password)
            Log.d("AuthViewModel", "Sign in result: $result")
            authState.value = result
            if (result.startsWith("Sign in successful")) {
                try {
                    val session = supabase.auth.currentSessionOrNull()
                    Log.d("AuthViewModel", "Updating session after sign in: $email")
                    sessionManager.updateSession(
                        isLoggedIn = true,
                        email = email,
                        userId = session?.user?.id ?: ""
                    )
                } catch (e: Exception) {
                    Log.e("AuthViewModel", "Error saving session after sign in: ${e.message}")
                }
            }
        }
    }

    fun signOut(onSignOutComplete: () -> Unit) {
        Log.d("AuthViewModel", "Signing out user")
        viewModelScope.launch {
            try {
                supabase.auth.signOut()
                sessionManager.clearSession()
                Log.d("AuthViewModel", "User signed out successfully")
                authState.value = ""
                onSignOutComplete()
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Sign out failed: ${e.message}")
            }
        }
    }
}