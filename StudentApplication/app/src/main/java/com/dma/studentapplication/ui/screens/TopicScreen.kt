package com.dma.studentapplication.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
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
private val DarkTextMuted = Color(0xFFB8C4E0)

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
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val backgroundColor = if (isDark) DarkBackground else LightBackground
    val cardColor = if (isDark) DarkCard else LightCard
    val textDark = if (isDark) DarkTextDark else LightTextDark
    val textMuted = if (isDark) DarkTextMuted else LightTextMuted

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                AppSideNavBar(
                    current = "topics",
                    onHomeClick = onHomeClick,
                    onTopicsClick = {},
                    onHistoryClick = onHistoryClick,
                    onProfileClick = onProfileClick,
                    isDark = isDark
                )

                TopicContent(
                    modifier = Modifier.weight(1f),
                    cardColor = cardColor,
                    textDark = textDark,
                    textMuted = textMuted,
                    onBack = onBack,
                    onTopicClick = onTopicClick
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                TopicContent(
                    modifier = Modifier.weight(1f),
                    cardColor = cardColor,
                    textDark = textDark,
                    textMuted = textMuted,
                    onBack = onBack,
                    onTopicClick = onTopicClick
                )

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
}

@Composable
private fun TopicContent(
    modifier: Modifier = Modifier,
    cardColor: Color,
    textDark: Color,
    textMuted: Color,
    onBack: () -> Unit,
    onTopicClick: (TopicItem) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            start = 24.dp,
            end = 24.dp,
            top = 30.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { onBack() },
                    tint = textDark
                )

                Text(
                    text = "All Topics",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = textDark,
                    modifier = Modifier.padding(start = 14.dp)
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
}

@Composable
private fun AppSideNavBar(
    current: String,
    onHomeClick: () -> Unit,
    onTopicsClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onProfileClick: () -> Unit,
    isDark: Boolean
) {
    val navBg = if (isDark) Color(0xFF071833) else Color.White
    val selected = Color(0xFF22C55E)
    val unselected = if (isDark) Color(0xFFB8C4E0) else Color(0xFF6B7280)

    Surface(
        modifier = Modifier
            .width(110.dp)
            .fillMaxHeight()
            .padding(start = 14.dp, top = 12.dp, bottom = 12.dp),
        color = navBg,
        shape = RoundedCornerShape(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            SideNavItem(
                title = "Home",
                icon = Icons.Default.Home,
                selected = current == "home",
                selectedColor = selected,
                unselectedColor = unselected,
                onClick = onHomeClick
            )

            SideNavItem(
                title = "Topics",
                icon = Icons.Default.GridView,
                selected = current == "topics",
                selectedColor = selected,
                unselectedColor = unselected,
                onClick = onTopicsClick
            )

            SideNavItem(
                title = "History",
                icon = Icons.Default.History,
                selected = current == "history",
                selectedColor = selected,
                unselectedColor = unselected,
                onClick = onHistoryClick
            )

            SideNavItem(
                title = "Profile",
                icon = Icons.Default.Person,
                selected = current == "profile",
                selectedColor = selected,
                unselectedColor = unselected,
                onClick = onProfileClick
            )
        }
    }
}

@Composable
private fun SideNavItem(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    selectedColor: Color,
    unselectedColor: Color,
    onClick: () -> Unit
) {
    val color = if (selected) selectedColor else unselectedColor

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = color,
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            color = color,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
        )
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
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(topic.color.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = topic.icon,
                    contentDescription = topic.title,
                    tint = topic.color,
                    modifier = Modifier.size(30.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = topic.title,
                    color = textDark,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "10 Questions • Practice Quiz",
                    color = textMuted,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Open topic",
                tint = textMuted,
                modifier = Modifier.size(18.dp)
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
