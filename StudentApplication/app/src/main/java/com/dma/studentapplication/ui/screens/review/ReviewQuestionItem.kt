package com.dma.studentapplication.ui.screens.review

import kotlinx.serialization.Serializable

/**
 * Represents the result of a single answered question, used to render
 * the per-question breakdown on [ReviewScreen] and [com.dma.studentapplication.ui.screens.history.HistoryDetailScreen].
 *
 * Annotated with [@Serializable] so kotlinx. Serialization can encode the
 * full list to a JSON string for storage in the Room `reviewJson` column,
 * and decode it back when loading history detail.
 *
 * @param question       The original question text.
 * @param selectedAnswer The answer the user chose, or "No answer selected" if they timed out.
 * @param correctAnswer  The text of the correct answer option.
 * @param isCorrect      True if [selectedAnswer] matches [correctAnswer].
 */
@Serializable
data class ReviewQuestionItem(
    val question: String,
    val selectedAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean
)