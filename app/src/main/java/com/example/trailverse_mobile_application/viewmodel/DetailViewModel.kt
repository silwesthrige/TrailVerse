package com.example.trailverse_mobile_application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trailverse_mobile_application.model.Comment
import com.example.trailverse_mobile_application.model.Location
import com.example.trailverse_mobile_application.repository.CommentRepository
import com.example.trailverse_mobile_application.repository.LocationRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val locationId: String) : ViewModel() {
    private val locationRepository = LocationRepository()
    private val commentRepository = CommentRepository()

    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    private val _userVote = MutableStateFlow(0)
    val userVote: StateFlow<Int> = _userVote

    init {
        viewModelScope.launch {
            locationRepository.getLocationFlow(locationId).collect { loc ->
                _location.value = loc
            }
        }
        viewModelScope.launch {
            commentRepository.getCommentsFlow(locationId).collect { list ->
                _comments.value = list
            }
        }
        loadUserVote()
    }

    private fun loadUserVote() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            _userVote.value = locationRepository.getUserVote(locationId, userId)
        }
    }

    fun vote(voteValue: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            locationRepository.vote(locationId, userId, voteValue)
            _userVote.value = if (_userVote.value == voteValue) 0 else voteValue
        }
    }

    fun postComment(text: String) {
        if (text.isBlank()) return
        val user = FirebaseAuth.getInstance().currentUser ?: return
        viewModelScope.launch {
            commentRepository.addComment(
                locationId,
                Comment(
                    userId = user.uid,
                    userName = user.displayName ?: "Anonymous",
                    text = text
                )
            )
        }
    }
}

class DetailViewModelFactory(private val locationId: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(locationId) as T
    }
}