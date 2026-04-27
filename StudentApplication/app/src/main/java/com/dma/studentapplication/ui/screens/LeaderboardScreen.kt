package com.dma.studentapplication.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.EmojiEvents
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
import androidx.compose.ui.unit.sp
import com.dma.studentapplication.ui.theme.StudentApplicationTheme

private val PrimaryGreen = Color(0xFF27D17F)
private val Gold = Color(0xFFFFD700)
private val Silver = Color(0xFFC0C0C0)
private val Bronze = Color(0xFFCD7F32)

private val LightBackground = Color(0xFFF3F4F6)
private val LightCard = Color.White
private val LightTextDark = Color(0xFF0B1B4A)
private val LightTextMuted = Color(0xFF5B6785)

private val DarkBackground = Color(0xFF000B1B)
private val DarkCard = Color(0xFF071833)
private val DarkTextDark = Color.White
private val DarkTextMuted = Color(0xFFB8C4E0)

@Composable
fun LeaderboardScreen(
    isDark: Boolean = isSystemInDarkTheme(),
    leaderboardItems: List<QuizHistoryItem> = emptyList(),
    onBackHome: () -> Unit = {},
    onTopicsClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val backgroundColor = if (isDark) DarkBackground else LightBackground
    val cardColor = if (isDark) DarkCard else LightCard
    val textDark = if (isDark) DarkTextDark else LightTextDark
    val textMuted = if (isDark) DarkTextMuted else LightTextMuted

    val sortedItems = leaderboardItems.sortedByDescending {
        val parts = it.score.split("/")
        if (parts.size == 2) {
            val score = parts[0].toDoubleOrNull() ?: 0.0
            val total = parts[1].toDoubleOrNull() ?: 1.0
            score / total
        } else {
            0.0
        }
    }.take(20)

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
                LeaderboardSideNavBar(
                    current = "leaderboard",
                    isDark = isDark,
                    onHomeClick = onBackHome,
                    onTopicsClick = onTopicsClick,
                    onHistoryClick = onHistoryClick,
                    onProfileClick = onProfileClick
                )

                LeaderboardContent(
                    modifier = Modifier.weight(1f),
                    sortedItems = sortedItems,
                    cardColor = cardColor,
                    textDark = textDark,
                    textMuted = textMuted,
                    onBackClick = onProfileClick
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                LeaderboardContent(
                    modifier = Modifier.weight(1f),
                    sortedItems = sortedItems,
                    cardColor = cardColor,
                    textDark = textDark,
                    textMuted = textMuted,
                    onBackClick = onProfileClick
                )

                AppBottomNavBar(
                    current = "profile",
                    onHomeClick = onBackHome,
                    onTopicsClick = onTopicsClick,
                    onHistoryClick = onHistoryClick,
                    onProfileClick = onProfileClick,
                    isDark = isDark
                )
            }
        }
    }
}

@Composable
private fun LeaderboardContent(
    modifier: Modifier = Modifier,
    sortedItems: List<QuizHistoryItem>,
    cardColor: Color,
    textDark: Color,
    textMuted: Color,
    onBackClick: () -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 18.dp,
            bottom = 28.dp
        )
    ) {
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back to Profile",
                        tint = textDark
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.EmojiEvents,
                        contentDescription = "Leaderboard",
                        tint = Gold,
                        modifier = Modifier.size(72.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Leaderboard",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = textDark
                    )

                    Text(
                        text = "Top 20 Local Scores",
                        style = MaterialTheme.typography.bodyMedium,
                        color = textMuted,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        if (sortedItems.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No scores yet!",
                            color = textMuted,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            itemsIndexed(sortedItems) { index, item ->
                LeaderboardItemCard(
                    rank = index + 1,
                    item = item,
                    cardColor = cardColor,
                    textDark = textDark,
                    textMuted = textMuted
                )
            }
        }
    }
}

@Composable
private fun LeaderboardSideNavBar(
    current: String,
    isDark: Boolean,
    onHomeClick: () -> Unit,
    onTopicsClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val navBg = if (isDark) Color(0xFF071833) else Color.White
    val selected = PrimaryGreen
    val unselected = if (isDark) Color(0xFFB8C4E0) else Color(0xFF6B7280)

    Surface(
        modifier = Modifier
            .width(110.dp)
            .fillMaxHeight()
            .padding(start = 14.dp, top = 12.dp, bottom = 12.dp),
        color = navBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            LeaderboardSideNavItem(
                title = "Home",
                icon = Icons.Default.Home,
                selected = current == "home",
                selectedColor = selected,
                unselectedColor = unselected,
                onClick = onHomeClick
            )

            LeaderboardSideNavItem(
                title = "Topics",
                icon = Icons.Default.GridView,
                selected = current == "topics",
                selectedColor = selected,
                unselectedColor = unselected,
                onClick = onTopicsClick
            )

            LeaderboardSideNavItem(
                title = "History",
                icon = Icons.Default.History,
                selected = current == "history",
                selectedColor = selected,
                unselectedColor = unselected,
                onClick = onHistoryClick
            )

            LeaderboardSideNavItem(
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
private fun LeaderboardSideNavItem(
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
fun LeaderboardItemCard(
    rank: Int,
    item: QuizHistoryItem,
    cardColor: Color,
    textDark: Color,
    textMuted: Color
) {
    val rankColor = when (rank) {
        1 -> Gold
        2 -> Silver
        3 -> Bronze
        else -> textMuted.copy(alpha = 0.65f)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(rankColor.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rank.toString(),
                    color = if (rank <= 3) rankColor else textDark,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.topic,
                    color = textDark,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = item.date,
                    color = textMuted,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                text = item.score,
                color = PrimaryGreen,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardPreview() {
    StudentApplicationTheme {
        LeaderboardScreen(
            leaderboardItems = listOf(
                QuizHistoryItem(1, "Technology", "23 Apr", "10/10"),
                QuizHistoryItem(2, "Science", "22 Apr", "9/10"),
                QuizHistoryItem(3, "Math", "21 Apr", "8/10")
            )
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    widthDp = 900,
    heightDp = 420
)
@Composable
fun LeaderboardLandscapePreview() {
    StudentApplicationTheme {
        LeaderboardScreen(
            leaderboardItems = listOf(
                QuizHistoryItem(1, "Technology", "23 Apr", "10/10"),
                QuizHistoryItem(2, "Science", "22 Apr", "9/10"),
                QuizHistoryItem(3, "Math", "21 Apr", "8/10")
            )
        )
    }
}