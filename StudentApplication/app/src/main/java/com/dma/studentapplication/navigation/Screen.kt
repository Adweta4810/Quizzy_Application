package com.dma.studentapplication.navigation

// ── Route definitions ─────────────────────────────────────────────────────────
sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object Topic : Screen("topic")

    data object Quiz : Screen("quiz/{topic}") {
        fun createRoute(topic: String): String = "quiz/$topic"
    }

    data object Review : Screen("review/{topic}/{score}/{total}") {
        fun createRoute(topic: String, score: Int, total: Int): String {
            return "review/$topic/$score/$total"
        }
    }
}