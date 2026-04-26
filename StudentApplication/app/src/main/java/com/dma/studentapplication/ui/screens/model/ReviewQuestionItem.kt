package com.dma.studentapplication.ui.screens.model

import kotlinx.serialization.Serializable

// ─── @Serializable so kotlinx can encode/decode to/from ROOM reviewJson ───────

@Serializable
data class ReviewQuestionItem(
    val question: String,
    val selectedAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean
)
