package com.youcef_bounaas.cibo.features.profile.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _name = MutableStateFlow(savedStateHandle.get<String>("name") ?: "")
    val name: StateFlow<String> get() = _name

    private val _username = MutableStateFlow(savedStateHandle.get<String>("username") ?: "")
    val username: StateFlow<String> get() = _username

    private val _email = MutableStateFlow(savedStateHandle.get<String>("email") ?: "")
    val email: StateFlow<String> get() = _email

    private val _password = MutableStateFlow(savedStateHandle.get<String>("password") ?: "")
    val password: StateFlow<String> get() = _password

    fun updateName(newName: String) {
        _name.value = newName
        savedStateHandle["name"] = newName
    }

    fun updateUsername(newUsername: String) {
        _username.value = newUsername
        savedStateHandle["username"] = newUsername
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
        savedStateHandle["email"] = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
        savedStateHandle["password"] = newPassword
    }

    fun deleteAccount() {
        // Logic to delete account
    }
}