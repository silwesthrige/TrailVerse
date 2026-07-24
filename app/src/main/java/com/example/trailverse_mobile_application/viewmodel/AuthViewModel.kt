package com.example.trailverse_mobile_application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailverse_mobile_application.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    fun isLoggedIn(): Boolean = repository.isLoggedIn()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState.Error("Please fill in all fields")
            return
        }
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            val result = repository.login(email, password)
            _uiState.value = result.fold(
                onSuccess = { AuthUiState.Success },
                onFailure = { AuthUiState.Error(it.message ?: "Login failed") }
            )
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState.Error("Please fill in all fields")
            return
        }
        if (password != confirmPassword) {
            _uiState.value = AuthUiState.Error("Passwords do not match")
            return
        }
        if (password.length < 6) {
            _uiState.value = AuthUiState.Error("Password must be at least 6 characters")
            return
        }
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            val result = repository.register(email, password, name)
            _uiState.value = result.fold(
                onSuccess = { AuthUiState.Success },
                onFailure = { AuthUiState.Error(it.message ?: "Registration failed") }
            )
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }

    fun logout() {
        repository.logout()
    }
}