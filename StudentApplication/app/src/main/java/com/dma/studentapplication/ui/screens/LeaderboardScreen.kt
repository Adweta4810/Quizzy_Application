package com.dma.studentapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dma.studentapplication.ui.theme.StudentApplicationTheme

// ─── Color tokens ─────────────────────────────────────────────────────────────

private val PrimaryGreen      = Color(0xFF27D17F)
private val Gold              = Color(0xFFFFD700)
private val Silver            = Color(0xFFC0C0C0)
private val Bronze            = Color(0xFFCD7F32)

private val LightBackground  = Color(0xFFF3F4F6)
private val LightCard        = Color(0xFFFFFFFF)
private val LightTextDark    = Color(0xFF0B1B4A)
private val LightTextMuted   = Color(0xFF5B6785)
private val LightNavBar      = Color(0xFFFFFFFF)

private val DarkBackground   = Color(0xFF000B1B)
private val DarkCard         = Color(0xFF071833)
private val DarkTextDark     = Color(0xFFFFFFFF)
private val DarkTextMuted    = Color(0xFF8FA3C8)
private val DarkNavBar       = Color(0xFF041225)

@Composable
fun LeaderboardScreen(
    isDark: Boolean = isSystemInDarkTheme(),
    leaderboardItems: List<QuizHistoryItem> = emptyList(),
    onBackHome: () -> Unit = {},
    onTopicsClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val backgroundColor = if (isDark) DarkBackground else LightBackground
    val cardColor       = if (isDark) DarkCard       else LightCard
    val navBarColor     = if (isDark) DarkNavBar     else LightNavBar
    val textDark        = if (isDark) DarkTextDark   else LightTextDark
    val textMuted       = if (isDark) DarkTextMuted  else LightTextMuted

    // Sort by score (parsing "8/10" to 0.8)
    val sortedItems = leaderboardItems.sortedByDescending {
        val parts = it.score.split("/")
        if (parts.size == 2) {
            val s = parts[0].toDoubleOrNull() ?: 0.0
            val t = parts[1].toDoubleOrNull() ?: 1.0
            s / t
        } else 0.0
    }.take(20)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = backgroundColor
    ) {
        Column(
            modifier            = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier            = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding      = PaddingValues(top = 32.dp, bottom = 96.dp)
            ) {
                // Header
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.EmojiEvents,
                            contentDescription = "Leaderboard",
                            tint = Gold,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text       = "Leaderboard",
                            style      = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color      = textDark
                        )
                        Text(
                            text     = "Top 20 Local Scores",
                            style    = MaterialTheme.typography.bodyMedium,
                            color    = textMuted
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                if (sortedItems.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No scores yet!", color = textMuted)
                        }
                    }
                } else {
                    itemsIndexed(sortedItems) { index, item ->
                        LeaderboardItemCard(
                            rank = index + 1,
                            item = item,
                            isDark = isDark,
                            cardColor = cardColor,
                            textDark = textDark,
                            textMuted = textMuted
                        )
                    }
                }
            }

            // Bottom nav
            NavigationBar(
                containerColor = navBarColor,
                tonalElevation = 0.dp
            ) {
                listOf(
                    Triple(Icons.Default.Home,     "Home",    false) to onBackHome,
                    Triple(Icons.Default.GridView,  "Topics",  false) to onTopicsClick,
                    Triple(Icons.Default.History,  "History", false) to onHistoryClick,
                    Triple(Icons.Default.Person,   "Profile", false) to onProfileClick,
                ).forEach { (triple, action) ->
                    val (icon, label, selected) = triple
                    NavigationBarItem(
                        selected = selected,
                        onClick  = action,
                        icon     = { Icon(icon, contentDescription = label) },
                        label    = { Text(label) },
                        colors   = NavigationBarItemDefaults.colors(
                            selectedIconColor   = PrimaryGreen,
                            selectedTextColor   = PrimaryGreen,
                            unselectedIconColor = if (isDark) DarkTextMuted else LightTextMuted,
                            unselectedTextColor = if (isDark) DarkTextMuted else LightTextMuted,
                            indicatorColor      = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun LeaderboardItemCard(
    rank: Int,
    item: QuizHistoryItem,
    isDark: Boolean,
    cardColor: Color,
    textDark: Color,
    textMuted: Color
) {
    val rankColor = when (rank) {
        1 -> Gold
        2 -> Silver
        3 -> Bronze
        else -> textMuted.copy(alpha = 0.5f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(rankColor.copy(alpha = 0.15f)),
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

            // Topic and Date
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

            // Score
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
