package com.dma.studentapplication.navigation

import android.net.Uri

sealed class Screen(val route: String) {

    object Splash : Screen("splash")

    object Nickname : Screen("nickname")

    object Home : Screen("home")

    object Topics : Screen("topics")

    object History : Screen("history")

    object Profile : Screen("profile")

    object Leaderboard : Screen("leaderboard")

    object Quiz : Screen("quiz/{topicId}/{topicTitle}") {
        fun createRoute(topicId: String, topicTitle: String): String {
            return "quiz/${Uri.encode(topicId)}/${Uri.encode(topicTitle)}"
        }
    }

    object Result : Screen("result/{resultId}/{score}/{total}/{timeTaken}/{topicTitle}") {
        fun createRoute(
            resultId: Long,
            score: Int,
            total: Int,
            timeTaken: String,
            topicTitle: String
        ): String {
            return "result/$resultId/$score/$total/${Uri.encode(timeTaken)}/${Uri.encode(topicTitle)}"
        }
    }

    object Review : Screen("review/{resultId}") {
        fun createRoute(resultId: Long): String {
            return "review/$resultId"
        }
    }

    object HistoryDetail : Screen("history_detail/{resultId}") {
        fun createRoute(resultId: Long): String {
            return "history_detail/$resultId"
        }
    }
}