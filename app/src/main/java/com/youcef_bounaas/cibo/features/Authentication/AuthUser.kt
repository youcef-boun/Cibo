package com.youcef_bounaas.cibo.features.Authentication


import com.youcef_bounaas.cibo.data.repository.SupabaseClientProvider.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put



suspend fun signUp(email: String, password: String, name: String, role : String): String {
    return try {
        val metadata = buildJsonObject {
            put("full_name", name)

        }

        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = password
            this.data = metadata
        }

        "Signup successful! Please check your email to verify your account before signing in."
    } catch (e: Exception) {
        "Signup failed: ${e.message}"
    }
}



suspend fun signIn(email: String, password: String): String {
    return try {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }

        "Sign in successful for user: ${email}"
    } catch (e: Exception) {
        if (e.message?.contains("Email not confirmed") == true) {
            "Please verify your email address before signing in. Check your inbox for the verification link."
        } else {
            "Sign in failed: ${e.message}"
        }
    }
}

