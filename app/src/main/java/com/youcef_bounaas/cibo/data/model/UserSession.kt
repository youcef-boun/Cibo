package com.youcef_bounaas.cibo.data.model


import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.catch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


@Serializable
data class UserSession(
    val isLoggedIn: Boolean = false,
    val userId: String = "",
    val email: String = "",
    val role: String = "customer" // "owner" or "customer"
)


object UserSessionSerializer : Serializer<UserSession> {
    override val defaultValue: UserSession = UserSession()

    override suspend fun readFrom(input: InputStream): UserSession {
        return try {
            val sessionData = input.readBytes().decodeToString()
            Log.d("SessionManager", "Read session data: $sessionData")  // Log read data
            Json.decodeFromString(
                UserSession.serializer(),
                sessionData
            )
        } catch (e: Exception) {
            Log.e("SessionManager", "Error reading session data: ${e.message}")  // Log error
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserSession, output: OutputStream) {
        val json = Json.encodeToString(UserSession.serializer(), t)
        Log.d("SessionManager", "Saving session data: $json")  // Log session being saved
        output.write(json.encodeToByteArray())
    }
}

class SessionManager private constructor(private val context: Context) {

    private val _userSession = MutableStateFlow(UserSession())
    val userSessions= _userSession.asStateFlow()
    private val _isSessionLoading = MutableStateFlow(true) // initially loading
    val isSessionLoading = _isSessionLoading.asStateFlow()

    init {
        // Your logic for session initialization, for example:
        // If fetching session data from shared preferences or remote server
        // After fetching:

        _isSessionLoading.value = false // finished loading
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: SessionManager? = null

        fun getInstance(context: Context): SessionManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SessionManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

    private val Context.userDataStore by dataStore(
        fileName = "user_session.json",
        serializer = UserSessionSerializer
    )

    val userSession = context.userDataStore.data
        .catch { exception ->
            if (exception is Exception) {
                emit(UserSession())
                Log.e("SessionManager", "Error fetching session: ${exception.message}")
            } else {
                throw exception
            }
        }

    suspend fun updateSession(
        isLoggedIn: Boolean,
        userId: String = "",
        email: String = ""
    ) {
        Log.d("SessionManager", "Updating session with isLoggedIn: $isLoggedIn, userId: $userId, email: $email")
        try {
            context.userDataStore.updateData {
                UserSession(isLoggedIn, userId, email)
            }
            Log.d("SessionManager", "Session updated successfully")
        } catch (e: Exception) {
            Log.e("SessionManager", "Error updating session: ${e.message}")
        }
    }

    suspend fun clearSession() {
        Log.d("SessionManager", "Clearing session")
        try {
            context.userDataStore.updateData {
                UserSession()
            }
            Log.d("SessionManager", "Session cleared successfully")
        } catch (e: Exception) {
            Log.e("SessionManager", "Error clearing session: ${e.message}")
        }
    }
}