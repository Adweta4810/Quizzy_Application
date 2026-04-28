package com.dma.studentapplication.ui.screens.home

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * UI model representing a single topic entry on the Home screen.
 *
 * Used by both the horizontal quick-topic chips and the vertical
 * "Continue Studying" topic list in [HomeScreen].
 *
 * @param id            Snake_case topic identifier used as the navigation argument (e.g. "current_affairs").
 * @param title         Human-readable display name shown below the icon (e.g. "Current Affairs").
 * @param icon          Icon displayed inside the topic card.
 * @param iconTint      Accent color applied to the icon.
 * @param lightBg       Icon background color used in light mode.
 * @param darkBg        Icon background color used in dark mode.
 * @param questionsLeft Number of questions remaining for this topic (reserved for future progress tracking).
 */
data class HomeTopicUi(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val iconTint: Color,
    val lightBg: Color,
    val darkBg: Color,
    val questionsLeft: Int
)