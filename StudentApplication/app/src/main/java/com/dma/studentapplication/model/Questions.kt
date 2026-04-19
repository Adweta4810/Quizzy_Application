package com.dma.studentapplication.model

import kotlinx.serialization.Serializable

// ── JSON models ──────────────────────────────────────────────────────────────

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

// ── Domain models ─────────────────────────────────────────────────────────────

data class QuizTopic(
    val id: String,
    val title: String,
    val fileName: String,
    val iconEmoji: String,
    val bgColorHex: String,   // pastel background
    val iconColorHex: String  // accent icon color
)

/** One answered question in a quiz attempt. */
data class AnsweredQuestion(
    val question: Question,
    val selectedIndex: Int,   // -1 = skipped / timed-out
    val timeTaken: Int        // seconds used
) {
    val isCorrect: Boolean get() = selectedIndex == question.correctAnswerIndex
    val wasSkipped: Boolean get() = selectedIndex == -1
}

/** Transient result passed from Quiz → Result → Review screens. */
data class QuizResult(
    val topicId: String,
    val topicTitle: String,
    val score: Int,
    val totalQuestions: Int,
    val xpEarned: Int,
    val timeTakenSeconds: Int,
    val answers: List<AnsweredQuestion>
) {
    val accuracyPercent: Int
        get() = if (totalQuestions == 0) 0 else (score * 100) / totalQuestions
}

/** Mascot emotional states. */
enum class MascotState {
    IDLE, WAVING, THINKING, CORRECT, WRONG, SURPRISED, CELEBRATING
}
