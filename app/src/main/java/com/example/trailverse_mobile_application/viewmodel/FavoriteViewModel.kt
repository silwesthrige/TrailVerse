package com.example.trailverse_mobile_application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailverse_mobile_application.repository.FavoriteRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {
    private val repository = FavoriteRepository()

    private val _savedIds = MutableStateFlow<Set<String>>(emptySet())
    val savedIds: StateFlow<Set<String>> = _savedIds

    init {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            viewModelScope.launch {
                repository.getSavedIdsFlow(userId).collect { ids ->
                    _savedIds.value = ids
                }
            }
        }
    }

    fun toggleSave(locationId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val isSaved = _savedIds.value.contains(locationId)
        viewModelScope.launch {
            repository.toggleSave(userId, locationId, isSaved)
        }
    }

    fun isSaved(locationId: String): Boolean = _savedIds.value.contains(locationId)
}