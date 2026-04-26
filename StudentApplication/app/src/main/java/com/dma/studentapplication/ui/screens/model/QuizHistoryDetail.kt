package com.dma.studentapplication.ui.screens.model

import com.dma.studentapplication.ui.screens.QuizHistoryItem

data class QuizHistoryDetail(
    val historyItem: QuizHistoryItem,
    val timeTaken: String,
    val totalQuestions: Int,
    val reviewItems: List<ReviewQuestionItem>
)