package com.dma.studentapplication.utils

data class Topic(
    val id: String,
    val title: String,
    val fileName: String
)

object Constants {
    val quizTopics = listOf(
        Topic("current_affairs", "Current Affairs", "current_affairs.json"),
        Topic("geography", "Geography", "geography.json"),
        Topic("technology", "Technology", "technology.json"),
        Topic("sports", "Sports", "sports.json"),
        Topic("history", "History", "history.json"),
        Topic("science", "Science", "science.json"),
        Topic("math", "Math", "math.json"),
        Topic("music", "Music", "music.json"),
        Topic("programming", "Programming", "programming.json"),
        Topic("networking", "Networking", "networking.json")
    )
}