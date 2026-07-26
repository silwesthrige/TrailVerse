package com.example.trailverse_mobile_application.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FavoriteRepository {
    private val db = FirebaseFirestore.getInstance()

    private fun savedRef(userId: String) =
        db.collection("users").document(userId).collection("saved")

    // Live set of saved location IDs for a user
    fun getSavedIdsFlow(userId: String): Flow<Set<String>> = callbackFlow {
        val listener = savedRef(userId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(emptySet())
                return@addSnapshotListener
            }
            val ids = snapshot?.documents?.map { it.id }?.toSet() ?: emptySet()
            trySend(ids)
        }
        awaitClose { listener.remove() }
    }

    suspend fun toggleSave(userId: String, locationId: String, isSaved: Boolean): Result<Unit> {
        return try {
            val docRef = savedRef(userId).document(locationId)
            if (isSaved) {
                docRef.delete().await()
            } else {
                docRef.set(mapOf("savedAt" to System.currentTimeMillis())).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}