package com.example.trailverse_mobile_application.repository

import com.example.trailverse_mobile_application.model.Location
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class LocationRepository {
    private val db = FirebaseFirestore.getInstance()
    private val locationsRef = db.collection("locations")

    // Live feed of all locations, newest first
    fun getLocationsFlow(): Flow<List<Location>> = callbackFlow {
        val listener = locationsRef
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val locations = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Location::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(locations)
            }
        awaitClose { listener.remove() }
    }

    suspend fun addLocation(location: Location): Result<String> {
        return try {
            val docRef = locationsRef.document()
            val toSave = location.copy(id = docRef.id, createdAt = System.currentTimeMillis())
            docRef.set(toSave).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLocationById(id: String): Result<Location> {
        return try {
            val doc = locationsRef.document(id).get().await()
            val location = doc.toObject(Location::class.java)?.copy(id = doc.id)
                ?: return Result.failure(Exception("Location not found"))
            Result.success(location)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Returns the current user's vote for a location: 1, -1, or 0 (none)
    suspend fun getUserVote(locationId: String, userId: String): Int {
        return try {
            val doc = locationsRef.document(locationId)
                .collection("votes").document(userId).get().await()
            (doc.getLong("value") ?: 0L).toInt()
        } catch (e: Exception) {
            0
        }
    }

    // Casts, changes, or removes a vote — all counts updated atomically in a transaction
    suspend fun vote(locationId: String, userId: String, newVote: Int): Result<Unit> {
        return try {
            val locationDoc = locationsRef.document(locationId)
            val voteDoc = locationDoc.collection("votes").document(userId)

            db.runTransaction { transaction ->
                val voteSnapshot = transaction.get(voteDoc)
                val previousVote = (voteSnapshot.getLong("value") ?: 0L).toInt()
                val locationSnapshot = transaction.get(locationDoc)
                var upvotes = (locationSnapshot.getLong("upvotes") ?: 0L).toInt()
                var downvotes = (locationSnapshot.getLong("downvotes") ?: 0L).toInt()

                // Undo previous vote's effect
                if (previousVote == 1) upvotes--
                if (previousVote == -1) downvotes--

                // Apply new vote (tapping the same arrow again clears it)
                val finalVote = if (previousVote == newVote) 0 else newVote
                if (finalVote == 1) upvotes++
                if (finalVote == -1) downvotes++

                transaction.update(locationDoc, mapOf("upvotes" to upvotes, "downvotes" to downvotes))
                if (finalVote == 0) {
                    transaction.delete(voteDoc)
                } else {
                    transaction.set(voteDoc, mapOf("value" to finalVote))
                }
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}