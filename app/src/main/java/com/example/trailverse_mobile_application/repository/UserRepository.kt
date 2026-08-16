package com.example.trailverse_mobile_application.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

data class UserStats(
    val contributions: Int = 0,
    val reputation: Int = 0
)

class UserRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getUserStats(userId: String): UserStats {
        return try {
            val snapshot = db.collection("locations")
                .whereEqualTo("createdBy", userId)
                .get()
                .await()

            val contributions = snapshot.size()
            val reputation = snapshot.documents.sumOf { doc ->
                (doc.getLong("upvotes") ?: 0L).toInt()
            }

            UserStats(contributions = contributions, reputation = reputation)
        } catch (e: Exception) {
            UserStats()
        }
    }

    fun getAvatarUrlFlow(userId: String): Flow<String> = callbackFlow {
        val listener = db.collection("users").document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend("")
                    return@addSnapshotListener
                }
                trySend(snapshot?.getString("avatarUrl") ?: "")
            }
        awaitClose { listener.remove() }
    }

    suspend fun saveAvatarUrl(userId: String, url: String): Result<Unit> {
        return try {
            db.collection("users").document(userId)
                .set(mapOf("avatarUrl" to url), com.google.firebase.firestore.SetOptions.merge())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}