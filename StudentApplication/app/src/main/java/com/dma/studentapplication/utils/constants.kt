package com.dma.studentapplication.utils

import com.dma.studentapplication.model.QuizTopic

/**
 * App-wide constants and configuration values.
 *
 * Centralizing these here means a single edit updates the behavior
 * everywhere — no magic numbers scattered across the codebase.
 */
object constants {

    // ── Quiz rules ────────────────────────────────────────────────────────────

    /** Number of questions shown per quiz session. */
    const val QUESTIONS_PER_QUIZ = 10

    /** Seconds allowed to answer each question before auto-locking. */
    const val TIMER_SECONDS = 15

    /** Timer turns red when seconds remaining falls below this value. */
    const val LOW_TIMER_THRESHOLD = 5


    // ── Topic catalogue ───────────────────────────────────────────────────────

    /**
     * Master list of all available quiz topics.
     *
     * Each [QuizTopic] entry maps a topic id to its display name, JSON asset
     * file, emoji icon, and light/dark accent colors used across the UI.
     *
     * Add or remove entries here to update the topic list globally.
     * The [JsonLoader] uses this list to resolve topic id → filename.
     */
    val quizTopics = listOf(
        QuizTopic("current_affairs", "Current Affairs", "current_affairs.json", "🌍", "#FEF3C7", "#F97316"),
        QuizTopic("geography",       "Geography",       "geography.json",       "🗺️", "#DCFCE7", "#22C55E"),
        QuizTopic("technology",      "Technology",      "technology.json",      "💻", "#CCFBF1", "#0D9488"),
        QuizTopic("sports",          "Sports",          "sports.json",          "⚽", "#FEE2E2", "#EF4444"),
        QuizTopic("history",         "History",         "history.json",         "🏛️", "#FEF3C7", "#F59E0B"),
        QuizTopic("science",         "Science",         "science.json",         "🔬", "#DBEAFE", "#3B82F6"),
        QuizTopic("math",            "Mathematics",     "math.json",            "📐", "#EDE9FE", "#8B5CF6"),
        QuizTopic("movies",          "Movies",          "movies.json",          "🎬", "#FCE7F3", "#EC4899"),
        QuizTopic("programming",     "Programming",     "programming.json",     "⌨️", "#E0E7FF", "#6366F1"),
        QuizTopic("networking",      "Networking",      "networking.json",      "🌐", "#CCFBF1", "#14B8A6")
    )

    // ── Greeting ──────────────────────────────────────────────────────────────

    /**
     * Returns a time-of-day greeting based on the device's current hour.
     *
     * Ranges:
     * - 05:00 – 11:59 → "Good Morning"
     * - 12:00 – 16:59 → "Good Afternoon"
     * - 17:00 – 20:59 → "Good Evening"
     * - 21:00 – 04:59 → "Hey Night Owl"
     */
    fun greeting(): String =
        when (java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)) {
            in 5..11  -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else      -> "Hey Night Owl"
        }

    // ── RoboBuddy mascot messages ─────────────────────────────────────────────
    // One message is picked at random from each list to vary the mascot's dialogue.

    /** Shown when the mascot is idle (no quiz active). */
    val idleMessages = listOf("Let's go! 🚀", "Ready to quiz?", "Pick a topic!")

    /** Shown immediately after a correct answer. */
    val correctMessages = listOf("Nice one! 🎯", "You got it!", "Brilliant! ⭐", "Nailed it! 🔥")

    /** Shown immediately after a wrong answer. */
    val wrongMessages = listOf("Not quite…", "Try again! 💪", "Almost there!", "Keep going!")

    /** Shown when the timer drops below [LOW_TIMER_THRESHOLD]. */
    val lowTimerMessages = listOf("Hurry up! ⏰", "Quick! Quick!", "Time's running out!")

    /** Shown when the user achieves a perfect score. */
    val celebrateMessages = listOf("You crushed it! 🎉", "Legendary! 🏆", "Perfect score!!")
}