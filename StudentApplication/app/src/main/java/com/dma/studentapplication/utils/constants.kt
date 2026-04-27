package com.dma.studentapplication.utils

import com.dma.studentapplication.model.QuizTopic

object constants {

    const val QUESTIONS_PER_QUIZ = 10
    const val TIMER_SECONDS      = 15
    const val LOW_TIMER_THRESHOLD = 5    // timer turns red below this
    const val XP_PER_CORRECT     = 10
    const val XP_BONUS_FULL      = 20   // bonus for 10/10

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

    // Dynamic greeting by hour
    fun greeting(): String = when (java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)) {
        in 5..11  -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        in 17..20 -> "Good Evening"
        else      -> "Hey Night Owl"
    }

    // Mascot messages
    val idleMessages    = listOf("Let's go! 🚀", "Ready to quiz?", "Pick a topic!")
    val correctMessages = listOf("Nice one! 🎯", "You got it!", "Brilliant! ⭐", "Nailed it! 🔥")
    val wrongMessages   = listOf("Not quite…", "Try again! 💪", "Almost there!", "Keep going!")
    val lowTimerMessages = listOf("Hurry up! ⏰", "Quick! Quick!", "Time's running out!")
    val celebrateMessages = listOf("You crushed it! 🎉", "Legendary! 🏆", "Perfect score!!")
}
