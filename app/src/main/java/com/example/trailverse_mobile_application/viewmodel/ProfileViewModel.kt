package com.example.trailverse_mobile_application.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trailverse_mobile_application.repository.CloudinaryRepository
import com.example.trailverse_mobile_application.repository.UserRepository
import com.example.trailverse_mobile_application.repository.UserStats
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(context: Context) : ViewModel() {
    private val repository = UserRepository()
    private val cloudinaryRepository = CloudinaryRepository(context.applicationContext)
    private val auth = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser? get() = auth.currentUser

    private val _stats = MutableStateFlow(UserStats())
    val stats: StateFlow<UserStats> = _stats

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _avatarUrl = MutableStateFlow("")
    val avatarUrl: StateFlow<String> = _avatarUrl

    private val _isUploadingAvatar = MutableStateFlow(false)
    val isUploadingAvatar: StateFlow<Boolean> = _isUploadingAvatar

    init {
        loadStats()
        loadAvatar()
    }

    private fun loadStats() {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            _isLoading.value = true
            _stats.value = repository.getUserStats(userId)
            _isLoading.value = false
        }
    }

    private fun loadAvatar() {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            repository.getAvatarUrlFlow(userId).collect { url ->
                _avatarUrl.value = url
            }
        }
    }

    fun uploadAvatar(uri: Uri) {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            _isUploadingAvatar.value = true
            val result = cloudinaryRepository.uploadImage(uri)
            if (result.isSuccess) {
                repository.saveAvatarUrl(userId, result.getOrDefault(""))
            }
            _isUploadingAvatar.value = false
        }
    }

    fun logout() {
        auth.signOut()
    }
}

class ProfileViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(context.applicationContext) as T
    }
}