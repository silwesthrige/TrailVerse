package com.example.trailverse_mobile_application.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trailverse_mobile_application.model.Location
import com.example.trailverse_mobile_application.repository.CloudinaryRepository
import com.example.trailverse_mobile_application.repository.LocationRepository
import com.example.trailverse_mobile_application.utils.GeocodingHelper
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AddLocationUiState {
    object Idle : AddLocationUiState()
    object Loading : AddLocationUiState()
    object Success : AddLocationUiState()
    data class Error(val message: String) : AddLocationUiState()
}

class LocationViewModel(context: Context) : ViewModel() {
    private val repository = LocationRepository()
    private val cloudinaryRepository = CloudinaryRepository(context.applicationContext)

    private val _locations = MutableStateFlow<List<Location>>(emptyList())
    val locations: StateFlow<List<Location>> = _locations

    private val _userVotes = MutableStateFlow<Map<String, Int>>(emptyMap())
    val userVotes: StateFlow<Map<String, Int>> = _userVotes

    private val _addState = MutableStateFlow<AddLocationUiState>(AddLocationUiState.Idle)
    val addState: StateFlow<AddLocationUiState> = _addState

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadLocations()
    }

    private fun loadLocations() {
        viewModelScope.launch {
            repository.getLocationsFlow().collect { list ->
                _locations.value = list
                _isLoading.value = false
                loadUserVotes(list)
            }
        }
    }

    private fun loadUserVotes(list: List<Location>) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            val votes = mutableMapOf<String, Int>()
            list.forEach { location ->
                votes[location.id] = repository.getUserVote(location.id, userId)
            }
            _userVotes.value = votes
        }
    }

    fun addLocation(
        context: Context,
        name: String,
        description: String,
        category: String,
        latitude: Double,
        longitude: Double,
        imageUri: Uri?
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        if (name.isBlank() || description.isBlank()) {
            _addState.value = AddLocationUiState.Error("Please fill in name and description")
            return
        }
        _addState.value = AddLocationUiState.Loading
        viewModelScope.launch {
            val alreadyExists = repository.locationNameExists(name)
            if (alreadyExists) {
                _addState.value = AddLocationUiState.Error(
                    "\"$name\" has already been added by another traveler. Try a more specific name."
                )
                return@launch
            }

            var imageUrl = ""
            if (imageUri != null) {
                val uploadResult = cloudinaryRepository.uploadImage(imageUri)
                if (uploadResult.isFailure) {
                    _addState.value = AddLocationUiState.Error(
                        "Image upload failed: ${uploadResult.exceptionOrNull()?.message}"
                    )
                    return@launch
                }
                imageUrl = uploadResult.getOrDefault("")
            }

            val city = GeocodingHelper.getCityName(context, latitude, longitude)

            val location = Location(
                name = name,
                description = description,
                category = category,
                imageUrl = imageUrl,
                latitude = latitude,
                longitude = longitude,
                city = city,
                createdBy = userId
            )
            val result = repository.addLocation(location)
            _addState.value = result.fold(
                onSuccess = { AddLocationUiState.Success },
                onFailure = { AddLocationUiState.Error(it.message ?: "Failed to add location") }
            )
        }
    }

    fun vote(locationId: String, voteValue: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            repository.vote(locationId, userId, voteValue)
            _userVotes.value = _userVotes.value.toMutableMap().apply {
                val current = this[locationId] ?: 0
                this[locationId] = if (current == voteValue) 0 else voteValue
            }
        }
    }

    fun resetAddState() {
        _addState.value = AddLocationUiState.Idle
    }
}

class LocationViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationViewModel(context.applicationContext) as T
    }
}