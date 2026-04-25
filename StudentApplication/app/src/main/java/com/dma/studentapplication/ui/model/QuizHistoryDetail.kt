package com.dma.studentapplication.ui.model

import com.dma.studentapplication.ui.screens.QuizHistoryItem
import com.dma.studentapplication.ui.screens.ReviewQuestionItem

data class QuizHistoryDetail(
    val historyItem: QuizHistoryItem,
    val timeTaken: String,
    val totalQuestions: Int,
    val reviewItems: List<ReviewQuestionItem>
)