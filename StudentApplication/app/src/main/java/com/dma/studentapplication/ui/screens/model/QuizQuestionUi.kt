package com.dma.studentapplication.ui.screens.model

import com.dma.studentapplication.utils.*
/**
 * UI model representing a single quiz question ready for display.
 *
 * Created by [JsonLoader] after shuffling the question pool and randomising
 * the option order. The correct answer is stored as text rather than an index
 * so it remains valid even after the options list is shuffled.
 *
 * @param id            1-based position of the question in the current session (used as a display label).
 * @param question      The question text shown to the user.
 * @param options       Answer choices in their shuffled display order.
 * @param correctAnswer The text of the correct answer — must match one entry in [options].
 */
data class QuizQuestionUi(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswer: String
)