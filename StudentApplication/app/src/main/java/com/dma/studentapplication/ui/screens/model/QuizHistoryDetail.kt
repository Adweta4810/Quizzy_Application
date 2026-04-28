package com.dma.studentapplication.ui.screens.model

import com.dma.studentapplication.ui.screens.QuizHistoryItem
import com.dma.studentapplication.ui.screens.*


/**
 * Full detail model for a completed quiz result shown on [HistoryDetailScreen].
 *
 * Combines the summary data from [QuizHistoryItem] with the additional
 * information needed to render the full answer breakdown.
 *
 * @param historyItem    Summary data (topic, date, score string) sourced from the history list.
 * @param timeTaken      Duration of the quiz session in "m:ss" format (e.g. "3:14").
 * @param totalQuestions Total number of questions in the quiz session.
 * @param reviewItems    Per-question review data used to render the answer breakdown list.
 */
data class QuizHistoryDetail(
    val historyItem: QuizHistoryItem,
    val timeTaken: String,
    val totalQuestions: Int,
    val reviewItems: List<ReviewQuestionItem>
)