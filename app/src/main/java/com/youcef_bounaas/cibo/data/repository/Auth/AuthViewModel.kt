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
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive


class AuthViewModel(
    @SuppressLint("StaticFieldLeak") private val context: Context
) : ViewModel() {

    val sessionManager = SessionManager.getInstance(context)
    private val supabase = SupabaseClientProvider.supabase
    var authState = mutableStateOf("")

    init {
        viewModelScope.launch {
            checkCurrentSession()
        }
    }


    private suspend fun fetchUserRole(userId: String): String {
        val result = supabase
            .from("users")
            .select(columns = Columns.list("role")) {
                filter { eq("id", userId) }
                single()
            }

        // Converting the response to a typed object
        return try {
            val jsonObject = result.data as JsonObject
            jsonObject["role"]?.jsonPrimitive?.content ?: "customer"
        } catch (e: Exception) {
            "customer"
        }
    }


    private suspend fun checkCurrentSession() {
        try {
            val session = supabase.auth.currentSessionOrNull()
            if (session != null) {
                val userId = session.user?.id ?: ""
                val role = fetchUserRole(userId)

                Log.d("AuthViewModel", "Existing session found: ${session.user?.email}")

                sessionManager.updateSession(
                    isLoggedIn = true,
                    email = session.user?.email ?: "",
                    userId = session.user?.id ?: "",
                    role = role
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



    fun signUpUser(email: String, password: String, name: String, role : String) {
        viewModelScope.launch {
            val result = signUp(email, password, name , role)
            authState.value = result
            if (result.startsWith("Signup successful")) {
                try {
                    val session = supabase.auth.currentSessionOrNull()
                    sessionManager.updateSession(
                        isLoggedIn = true,
                        email = email,
                        userId = session?.user?.id ?: "",
                        role = "customer" // Use default role
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
                    val userId = session?.user?.id ?: ""
                    val role = fetchUserRole(userId) // Fetch role after sign-in

                    Log.d("AuthViewModel", "Updating session after sign in: $email")

                    sessionManager.updateSession(
                        isLoggedIn = true,
                        email = email,
                        userId = session?.user?.id ?: "",
                        role = role // Pass the fetched role
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