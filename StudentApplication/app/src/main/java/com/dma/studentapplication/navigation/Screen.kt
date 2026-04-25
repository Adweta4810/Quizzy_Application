package com.dma.studentapplication.navigation

import android.net.Uri

/**
 * Sealed class that holds every navigation route in the app.
 *
 * Routes that carry arguments define a [route] template with `{placeholder}`
 * segments AND a typed `createRoute()` helper so call-sites never
 * hand-assemble strings.
 *
 * Topic titles may contain spaces, so they are URL-encoded with [Uri.encode]
 * before being embedded in a route string.
 */
sealed class Screen(val route: String) {

    /** Animated splash / onboarding screen. */
    object Splash : Screen("splash")

    /** Home screen showing topic grid + daily quiz card. */
    object Home : Screen("home")

    /** Topics browser (used by HistoryScreen bottom-nav "Topics" tab). */
    object Topics : Screen("topics")

    /** User profile screen. */
    object Profile : Screen("profile")

    /**
     * Active quiz screen.
     * Args: topicId (String), topicTitle (String — URL-encoded).
     */
    object Quiz : Screen("quiz/{topicId}/{topicTitle}") {
        fun createRoute(topicId: String, topicTitle: String): String =
            "quiz/${Uri.encode(topicId)}/${Uri.encode(topicTitle)}"
    }

    /**
     * Post-quiz result / score screen.
     * Args: score (Int), total (Int), timeTaken (String), topicTitle (String).
     */
    object Result : Screen("result/{score}/{total}/{timeTaken}/{topicTitle}") {
        fun createRoute(
            score: Int,
            total: Int,
            timeTaken: String,
            topicTitle: String
        ): String = "result/$score/$total/${Uri.encode(timeTaken)}/${Uri.encode(topicTitle)}"
    }

    /**
     * Per-question review screen reached from ResultScreen.
     * Args: resultId (Long) — the ROOM primary key.
     */
    object Review : Screen("review/{resultId}") {
        fun createRoute(resultId: Long): String = "review/$resultId"
    }

    /**
     * History list screen.
     * No arguments — reads all ROOM rows via ViewModel Flow.
     */
    object History : Screen("history")

    /**
     * History detail screen — same content as Review but reached from History.
     * Args: resultId (Long) — the ROOM primary key.
     */
    object HistoryDetail : Screen("history_detail/{resultId}") {
        fun createRoute(resultId: Long): String = "history_detail/$resultId"
    }
}