package com.example.trailverse_mobile_application.repository

import com.example.trailverse_mobile_application.model.Comment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CommentRepository {
    private val db = FirebaseFirestore.getInstance()

    private fun commentsRef(locationId: String) =
        db.collection("locations").document(locationId).collection("comments")

    fun getCommentsFlow(locationId: String): Flow<List<Comment>> = callbackFlow {
        val listener = commentsRef(locationId)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val comments = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Comment::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(comments)
            }
        awaitClose { listener.remove() }
    }

    suspend fun addComment(locationId: String, comment: Comment): Result<Unit> {
        return try {
            val docRef = commentsRef(locationId).document()
            val toSave = comment.copy(id = docRef.id, locationId = locationId, timestamp = System.currentTimeMillis())
            docRef.set(toSave).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}