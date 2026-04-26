package com.dma.studentapplication.ui.screens.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class HomeTopicUi(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val iconTint: Color,
    val lightBg: Color,
    val darkBg: Color,
    val questionsLeft: Int
)