package com.dma.studentapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

@Serializable
data class QuestionList(
    val questions: List<Question>
)