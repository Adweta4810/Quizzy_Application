package com.dma.studentapplication.data.local.room

import androidx.room3.Entity
import androidx.room3.PrimaryKey

/**
 * Persisted quiz attempt stored in the ROOM database.
 *
 * [answersJson] is a JSON-serialized list of [AnsweredQuestion] objects
 * so we avoid a separate table while keeping full answer detail.
 */
@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val topicId: String,
    val topicTitle: String,
    val score: Int,
    val totalQuestions: Int,
    val xpEarned: Int,
    val timeTakenSeconds: Int,
    val dateTaken: Long = System.currentTimeMillis(),  // epoch ms

    /** Serialised list of answers — see QuizRepository for encoding. */
    val answersJson: String = "[]"
)
