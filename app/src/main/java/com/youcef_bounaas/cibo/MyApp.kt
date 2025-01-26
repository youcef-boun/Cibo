package com.youcef_bounaas.cibo

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

import com.youcef_bounaas.cibo.test.SessionManager
import com.youcef_bounaas.cibo.data.repository.SupabaseClientProvider
import com.youcef_bounaas.cibo.data.repository.SupabaseClientProvider.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.supabaseJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val sessionManager = SessionManager(this)
        val refreshToken = sessionManager.getRefreshToken()

        if (refreshToken != null) {
            restoreSession(refreshToken, sessionManager)
        } else {
            Log.d("MyApp", "No refresh token found")
        }
    }

    private fun restoreSession(refreshToken: String, sessionManager: SessionManager) {
        val supabaseClient = SupabaseClientProvider.supabase

        CoroutineScope(Dispatchers.IO).launch {
            try {
                supabaseClient.auth.refreshSession(refreshToken) // This refreshes the session
                val session = supabaseClient.auth.currentSessionOrNull()
                if (session != null) {
                    // Save new tokens if necessary

                    sessionManager.saveSession(
                        accessToken = session.accessToken,
                        refreshToken = session.refreshToken
                    )
                    Log.d("MyApp", "Session restored: ${session.user?.email}")
                }
            } catch (e: Exception) {
                Log.e("MyApp", "Error restoring session: ${e.message}")
            }
        }
    }
}
