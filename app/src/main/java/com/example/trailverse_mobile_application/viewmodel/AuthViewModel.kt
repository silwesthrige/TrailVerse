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

sealed class ResetPasswordUiState {
    object Idle : ResetPasswordUiState()
    object Loading : ResetPasswordUiState()
    object Success : ResetPasswordUiState()
    data class Error(val message: String) : ResetPasswordUiState()
}

sealed class DeleteAccountUiState {
    object Idle : DeleteAccountUiState()
    object Loading : DeleteAccountUiState()
    object Success : DeleteAccountUiState()
    data class Error(val message: String) : DeleteAccountUiState()
}

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    private val _resetPasswordState = MutableStateFlow<ResetPasswordUiState>(ResetPasswordUiState.Idle)
    val resetPasswordState: StateFlow<ResetPasswordUiState> = _resetPasswordState

    private val _deleteAccountState = MutableStateFlow<DeleteAccountUiState>(DeleteAccountUiState.Idle)
    val deleteAccountState: StateFlow<DeleteAccountUiState> = _deleteAccountState

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

    fun sendPasswordReset(email: String) {
        if (email.isBlank()) {
            _resetPasswordState.value = ResetPasswordUiState.Error("Please enter your email")
            return
        }
        _resetPasswordState.value = ResetPasswordUiState.Loading
        viewModelScope.launch {
            val result = repository.sendPasswordResetEmail(email)
            _resetPasswordState.value = result.fold(
                onSuccess = { ResetPasswordUiState.Success },
                onFailure = { ResetPasswordUiState.Error(it.message ?: "Failed to send reset email") }
            )
        }
    }

    fun resetPasswordResetState() {
        _resetPasswordState.value = ResetPasswordUiState.Idle
    }

    fun deleteAccount(password: String) {
        if (password.isBlank()) {
            _deleteAccountState.value = DeleteAccountUiState.Error("Please enter your password")
            return
        }
        _deleteAccountState.value = DeleteAccountUiState.Loading
        viewModelScope.launch {
            val result = repository.deleteAccount(password)
            _deleteAccountState.value = result.fold(
                onSuccess = { DeleteAccountUiState.Success },
                onFailure = { DeleteAccountUiState.Error(it.message ?: "Failed to delete account") }
            )
        }
    }

    fun resetDeleteAccountState() {
        _deleteAccountState.value = DeleteAccountUiState.Idle
    }
}