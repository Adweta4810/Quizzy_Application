package com.dma.studentapplication.ui.screens.model

data class QuizQuestionUi(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswer: String
)