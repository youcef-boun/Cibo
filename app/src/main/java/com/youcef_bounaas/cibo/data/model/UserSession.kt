package com.youcef_bounaas.cibo.data.model




import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream


@Serializable
data class UserSession(
    val isLoggedIn: Boolean = false,
    val userId: String = "",
    val email: String = "",
    val role: String = "customer"
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
        withContext(Dispatchers.IO) {
            output.write(json.encodeToByteArray())
        }
    }
}

class SessionManager private constructor(private val context: Context) {

    private val _userSession = MutableStateFlow(UserSession())
    val userSessions= _userSession.asStateFlow()
    private val _isSessionLoading = MutableStateFlow(true) // initially loading
    val isSessionLoading = _isSessionLoading.asStateFlow()

    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = prefs.edit()

    init {

        _isSessionLoading.value = false
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
        email: String = "",
        role: String

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
        editor.clear().apply()
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


    private val sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)

    fun getUserId(): String {
        return sharedPreferences.getString("USER_ID", "") ?: ""
    }

    fun getEmail(): String {
        return sharedPreferences.getString("EMAIL", "") ?: ""
    }







}
