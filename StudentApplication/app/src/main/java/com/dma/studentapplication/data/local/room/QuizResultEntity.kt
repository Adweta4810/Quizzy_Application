package com.dma.studentapplication.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ROOM entity that stores one completed quiz session.
 *
 * [reviewJson] is a Gson-serialized `List<ReviewQuestionItem>` so all
 * per-question answer data is stored without needing a second table.
 */
@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /** Display name of the topic, e.g. "Programming Quiz". */
    val topic: String,

    /** Human-readable date string, e.g. "23 Apr 2026". */
    val date: String,

    /** Number of correct answers. */
    val score: Int,

    /** Total number of questions in the quiz (always 10). */
    val totalQuestions: Int,

    /** Duration string formatted as "m:ss", e.g. "3:42". */
    val timeTaken: String,

    /**
     * Gson JSON of `List<ReviewQuestionItem>` — stores every question,
     * the user's selected answer, the correct answer, and whether it was right.
     */
    val reviewJson: String
)