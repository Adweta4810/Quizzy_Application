package com.dma.studentapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dma.studentapplication.ui.theme.StudentApplicationTheme

private val LightBackground = Color(0xFFF3F4F6)
private val LightCard = Color.White
private val LightTextDark = Color(0xFF0B1B4A)
private val LightTextMuted = Color(0xFF5B6785)

private val DarkBackground = Color(0xFF000B1B)
private val DarkCard = Color(0xFF071833)
private val DarkTextDark = Color.White
private val DarkTextMuted = Color(0xFF8FA3C8)

data class TopicItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val color: Color
)

val topics = listOf(
    TopicItem("current_affairs", "Current Affairs", Icons.Default.Newspaper, Color(0xFFEF4444)),
    TopicItem("geography", "Geography", Icons.Default.Public, Color(0xFF14B87A)),
    TopicItem("history", "History", Icons.Outlined.AutoStories, Color(0xFF4B83FF)),
    TopicItem("math", "Math", Icons.Default.Calculate, Color(0xFF22C55E)),
    TopicItem("movies", "Movies", Icons.Default.LocalMovies, Color(0xFFF45B87)),
    TopicItem("networking", "Networking", Icons.Default.Router, Color(0xFF0EA5E9)),
    TopicItem("programming", "Programming", Icons.Default.Code, Color(0xFF10CBE8)),
    TopicItem("science", "Science", Icons.Default.Science, Color(0xFF8B5CF6)),
    TopicItem("sports", "Sports", Icons.Default.SportsBasketball, Color(0xFFF59E0B)),
    TopicItem("technology", "Technology", Icons.Default.Devices, Color(0xFF6366F1))
)

@Composable
fun TopicScreen(
    isDark: Boolean = isSystemInDarkTheme(),
    onTopicClick: (TopicItem) -> Unit = {},
    onBack: () -> Unit = {},
    onHomeClick: () -> Unit = onBack,
    onHistoryClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val backgroundColor = if (isDark) DarkBackground else LightBackground
    val cardColor = if (isDark) DarkCard else LightCard
    val textDark = if (isDark) DarkTextDark else LightTextDark
    val textMuted = if (isDark) DarkTextMuted else LightTextMuted

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 30.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier
                                .size(26.dp)
                                .clickable { onBack() },
                            tint = textDark
                        )

                        Text(
                            text = "All Topics",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = textDark,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }

                items(topics) { topic ->
                    TopicCard(
                        topic = topic,
                        cardColor = cardColor,
                        textDark = textDark,
                        textMuted = textMuted,
                        onClick = { onTopicClick(topic) }
                    )
                }
            }

            AppBottomNavBar(
                current = "topics",
                onHomeClick = onHomeClick,
                onTopicsClick = {},
                onHistoryClick = onHistoryClick,
                onProfileClick = onProfileClick,
                isDark = isDark
            )
        }
    }
}

@Composable
fun TopicCard(
    topic: TopicItem,
    cardColor: Color,
    textDark: Color,
    textMuted: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(topic.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = topic.icon,
                    contentDescription = topic.title,
                    tint = topic.color,
                    modifier = Modifier.size(26.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = topic.title,
                    color = textDark,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "10 Questions • Practice Quiz",
                    color = textMuted,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Go",
                tint = textMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TopicScreenPreview() {
    StudentApplicationTheme {
        TopicScreen()
    }
}
