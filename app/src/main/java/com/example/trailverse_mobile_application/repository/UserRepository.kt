package com.example.trailverse_mobile_application.repository

import com.google.firebase.firestore.FirebaseFirestore
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
}