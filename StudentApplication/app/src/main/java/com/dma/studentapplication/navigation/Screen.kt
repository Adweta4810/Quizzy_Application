package com.dma.studentapplication.navigation

// ── Route definitions ─────────────────────────────────────────────────────────

sealed class Screen(val route: String) {
    object Home    : Screen("home")
    object Topics  : Screen("topics")
    object Quiz    : Screen("quiz/{topicId}") {
        fun createRoute(topicId: String) = "quiz/$topicId"
    }
    object Result  : Screen("result")    // receives QuizResult via ViewModel
    object Review  : Screen("review")    // receives answers via ViewModel
    object History : Screen("history")
    object HistoryDetail : Screen("history_detail/{resultId}") {
        fun createRoute(resultId: Int) = "history_detail/$resultId"
    }
}
