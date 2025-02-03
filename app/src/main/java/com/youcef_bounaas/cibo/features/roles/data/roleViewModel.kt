package com.youcef_bounaas.cibo.features.roles.data


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youcef_bounaas.cibo.data.model.SessionManager
import com.youcef_bounaas.cibo.data.repository.SupabaseClientProvider
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.util.UUID



class UsersViewModel(
    @SuppressLint("StaticFieldLeak") private val context: Context,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val supabase = SupabaseClientProvider.supabase
    private val sessionManager = SessionManager.getInstance(context)
    private val _users = MutableStateFlow<List<UserData>>(emptyList())
    val users = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                Log.d("UsersViewModel", "Starting to fetch users...")

                val response = supabase
                    .from("users")
                    .select()

                Log.d("UsersViewModel", "Raw response: ${response.data}")
                Log.d("UsersViewModel", "Response headers: ${response.headers}")

                // Parse the response string into a JsonArray
                val json = Json { ignoreUnknownKeys = true }
                val jsonString = response.data
                val usersList = json.decodeFromString<List<UserData>>(jsonString)

                Log.d("UsersViewModel", "Parsed ${usersList.size} users")
                _users.value = usersList

            } catch (e: Exception) {
                Log.e("UsersViewModel", "Error loading users: ${e.message}", e)
                _error.value = "Failed to load users: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createInitialUser() {
        viewModelScope.launch {
            try {
                // Get current user info from SessionManager
                val userId = sessionManager.getUserId()
                val userEmail = sessionManager.getEmail()

                if (userId.isEmpty() || userEmail.isEmpty()) {
                    _error.value = "User not logged in"
                    return@launch
                }

                Log.d("UsersViewModel", "Creating user with ID: $userId and email: $userEmail")

                val userJson = JsonObject(mapOf(
                    "id" to JsonPrimitive(userId),
                    "email" to JsonPrimitive(userEmail),
                    "role_id" to JsonPrimitive(UUID.randomUUID().toString()),
                    "role" to JsonPrimitive("customer") // Default role for new users
                ))

                Log.d("UsersViewModel", "Inserting user with data: $userJson")

                val response = supabase
                    .from("users")
                    .insert(userJson)

                Log.d("UsersViewModel", "Insert response data: ${response.data}")
                Log.d("UsersViewModel", "Insert response headers: ${response.headers}")

                loadUsers()
            } catch (e: Exception) {
                Log.e("UsersViewModel", "Error creating initial user: ${e.message}", e)
                _error.value = "Failed to create initial user: ${e.message}"
            }
        }
    }

    private val roleToIdMapping = mapOf(
        "admin" to "0e614595-852a-46be-88a3-63ed983868ea",
        "staff" to "66f5f91e-95f2-4637-9711-4a1188388a5f",
        "delivery_driver" to "e5822b8a-400e-4c77-9937-328181769557",
        "customer" to "5b3f8019-0601-480c-b9fe-d070facfdfac"
    )

    fun updateUserRole(userId: String, newRole: String) {
        viewModelScope.launch {
            try {
                Log.d("UsersViewModel", "Updating role for user $userId to $newRole")

                // Get the correct role_id for the new role
                val roleId = roleToIdMapping[newRole] ?: run {
                    _error.value = "Invalid role selected"
                    return@launch
                }

                val updateJson = JsonObject(mapOf(
                    "role" to JsonPrimitive(newRole),
                    "role_id" to JsonPrimitive(roleId)  // Use the correct role_id
                ))

                Log.d("UsersViewModel", "Updating with data: $updateJson")

                val response = supabase
                    .from("users")
                    .update(updateJson) {
                        filter { eq("id", userId) }
                    }

                Log.d("UsersViewModel", "Update response data: ${response.data}")
                Log.d("UsersViewModel", "Update response headers: ${response.headers}")

                loadUsers() // Reload the users list after update

                // Update session if the updated user is the current user
                if (userId == sessionManager.getUserId()) {
                    sessionManager.updateSession(
                        isLoggedIn = true,
                        email = sessionManager.getEmail(),
                        userId = userId,
                        role = newRole
                    )
                }
            } catch (e: Exception) {
                Log.e("UsersViewModel", "Error updating role: ${e.message}", e)
                _error.value = "Failed to update role: ${e.message}"
            }
        }
    }


}
@Serializable
data class UserData(
    val id: String,
    val email: String,
    val role_id: String,
    val role: String
)