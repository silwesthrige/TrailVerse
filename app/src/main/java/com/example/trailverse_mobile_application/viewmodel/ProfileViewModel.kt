package com.example.trailverse_mobile_application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailverse_mobile_application.repository.UserRepository
import com.example.trailverse_mobile_application.repository.UserStats
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val repository = UserRepository()
    private val auth = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser? get() = auth.currentUser

    private val _stats = MutableStateFlow(UserStats())
    val stats: StateFlow<UserStats> = _stats

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadStats()
    }

    private fun loadStats() {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            _isLoading.value = true
            _stats.value = repository.getUserStats(userId)
            _isLoading.value = false
        }
    }

    fun logout() {
        auth.signOut()
    }
}