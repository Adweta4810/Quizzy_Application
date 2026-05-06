package com.dma.studentapplication.ui.screens.profile

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
import com.dma.studentapplication.ui.components.AppBottomNavBar
import com.dma.studentapplication.ui.screens.history.QuizHistoryItem
import com.dma.studentapplication.ui.theme.StudentApplicationTheme

// ── Theme colors ──────────────────────────────────────────────────────────────

private val PrimaryGreen = Color(0xFF27D17F) // Active/selected color and score text
private val Gold         = Color(0xFFFFD700) // 1st place rank color
private val Silver       = Color(0xFFC0C0C0) // 2nd place rank color
private val Bronze       = Color(0xFFCD7F32) // 3rd place rank color

// Light mode
private val LightBackground = Color(0xFFF3F4F6)
private val LightCard       = Color.White
private val LightTextDark   = Color(0xFF0B1B4A)
private val LightTextMuted  = Color(0xFF5B6785)

// Dark mode
private val DarkBackground = Color(0xFF000B1B)
private val DarkCard       = Color(0xFF071833)
private val DarkTextDark   = Color.White
private val DarkTextMuted  = Color(0xFFB8C4E0)

// ── Main screen ───────────────────────────────────────────────────────────────

/**
 * Displays the top 20 local quiz scores sorted by score percentage (highest first).
 *
 * Adapts layout for portrait (bottom nav bar) and landscape (side nav rail) orientations.
 *
 * @param isDark           Whether dark mode is active. Defaults to system setting.
 * @param leaderboardItems Full list of quiz results to rank. Only the top 20 are shown.
 * @param onBackHome       Called when the Home nav item or back arrow is tapped.
 * @param onTopicsClick    Called when the Topics nav item is tapped.
 * @param onHistoryClick   Called when the History nav item is tapped.
 * @param onProfileClick   Called when the Profile nav item is tapped.
 */
@Composable
fun LeaderboardScreen(
    isDark: Boolean = isSystemInDarkTheme(),
    leaderboardItems: List<QuizHistoryItem> = emptyList(),
    onBackHome: () -> Unit = {},
    onTopicsClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    // ── Theme-aware colors ────────────────────────────────────────────────────
    val backgroundColor = if (isDark) DarkBackground else LightBackground
    val cardColor       = if (isDark) DarkCard       else LightCard
    val textDark        = if (isDark) DarkTextDark   else LightTextDark
    val textMuted       = if (isDark) DarkTextMuted  else LightTextMuted

    // Sort by score percentage descending and cap at 20 entries
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
        color    = backgroundColor
    ) {
        if (isLandscape) {
            // Landscape: side nav rail on the left, ranked list fills the rest
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                LeaderboardSideNavBar(
                    current        = "leaderboard",
                    isDark         = isDark,
                    onHomeClick    = onBackHome,
                    onTopicsClick  = onTopicsClick,
                    onHistoryClick = onHistoryClick,
                    onProfileClick = onProfileClick
                )

                LeaderboardContent(
                    modifier    = Modifier.weight(1f),
                    sortedItems = sortedItems,
                    cardColor   = cardColor,
                    textDark    = textDark,
                    textMuted   = textMuted,
                    onBackClick = onBackHome // Back arrow navigates home in landscape
                )
            }
        } else {
            // Portrait: content on top, bottom nav bar below
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                LeaderboardContent(
                    modifier    = Modifier.weight(1f),
                    sortedItems = sortedItems,
                    cardColor   = cardColor,
                    textDark    = textDark,
                    textMuted   = textMuted,
                    onBackClick = onBackHome// Back arrow returns to profile in portrait
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

// ── Scrollable content ────────────────────────────────────────────────────────

/**
 * Scrollable body of the leaderboard screen.
 *
 * Contains:
 * - Back arrow and trophy header with title
 * - Empty state card when no scores exist
 * - Ranked list of [LeaderboardItemCard]s when scores are available
 *
 * @param sortedItems Pre-sorted and capped list of history items to display.
 * @param onBackClick Called when the back arrow is tapped.
 */
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
        modifier            = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding      = PaddingValues(
            start  = 24.dp,
            end    = 24.dp,
            top    = 24.dp,
            bottom = 24.dp
        )
    ) {
        // Header — back arrow + trophy icon + title
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector        = Icons.Default.ArrowBack,
                        contentDescription = "Back to Profile",
                        tint               = textDark
                    )
                }

                Column(
                    modifier            = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector        = Icons.Outlined.EmojiEvents,
                        contentDescription = "Leaderboard",
                        tint               = Gold,
                        modifier           = Modifier.size(72.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text       = "Leaderboard",
                        style      = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color      = textDark
                    )

                    Text(
                        text       = "Top 20 Local Scores",
                        style      = MaterialTheme.typography.bodyMedium,
                        color      = textMuted,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        // Empty state — shown when no quiz results exist yet
        if (sortedItems.isEmpty()) {
            item {
                Card(
                    modifier  = Modifier.fillMaxWidth(),
                    shape     = RoundedCornerShape(22.dp),
                    colors    = CardDefaults.cardColors(containerColor = cardColor),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Box(
                        modifier         = Modifier.fillMaxWidth().padding(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = "No scores yet!",
                            color      = textMuted,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            // Ranked list — index + 1 gives the 1-based rank for each card
            itemsIndexed(sortedItems) { index, item ->
                LeaderboardItemCard(
                    rank      = index + 1,
                    item      = item,
                    cardColor = cardColor,
                    textDark  = textDark,
                    textMuted = textMuted
                )
            }
        }
    }
}

// ── Navigation ────────────────────────────────────────────────────────────────

/**
 * Landscape side navigation rail for the leaderboard screen.
 * No item is highlighted as "leaderboard" is not a standard bottom nav destination.
 *
 * @param current Key of the currently active screen (used to highlight the correct item).
 */
@Composable
private fun LeaderboardSideNavBar(
    current: String,
    isDark: Boolean,
    onHomeClick: () -> Unit,
    onTopicsClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val navBg     = if (isDark) Color(0xFF071833) else Color.White
    val selected  = PrimaryGreen
    val unselected = if (isDark) Color(0xFFB8C4E0) else Color(0xFF6B7280)

    Surface(
        modifier = Modifier
            .width(110.dp)
            .fillMaxHeight()
            .padding(start = 14.dp, top = 12.dp, bottom = 12.dp),
        color = navBg
    ) {
        Column(
            modifier              = Modifier.fillMaxSize().padding(vertical = 18.dp),
            horizontalAlignment   = Alignment.CenterHorizontally,
            verticalArrangement   = Arrangement.SpaceEvenly
        ) {
            LeaderboardSideNavItem(
                title           = "Home",
                icon            = Icons.Default.Home,
                selected        = current == "home",
                selectedColor   = selected,
                unselectedColor = unselected,
                onClick         = onHomeClick
            )

            LeaderboardSideNavItem(
                title           = "Topics",
                icon            = Icons.Default.GridView,
                selected        = current == "topics",
                selectedColor   = selected,
                unselectedColor = unselected,
                onClick         = onTopicsClick
            )

            LeaderboardSideNavItem(
                title           = "History",
                icon            = Icons.Default.History,
                selected        = current == "history",
                selectedColor   = selected,
                unselectedColor = unselected,
                onClick         = onHistoryClick
            )

            LeaderboardSideNavItem(
                title           = "Profile",
                icon            = Icons.Default.Person,
                selected        = current == "profile",
                selectedColor   = selected,
                unselectedColor = unselected,
                onClick         = onProfileClick
            )
        }
    }
}

/** Single item inside [LeaderboardSideNavBar]. Highlights in green when selected. */
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
        modifier            = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = title,
            tint               = color,
            modifier           = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text       = title,
            color      = color,
            fontWeight = FontWeight.Bold,
            style      = MaterialTheme.typography.bodyMedium
        )
    }
}

// ── Leaderboard item card ─────────────────────────────────────────────────────

/**
 * Single ranked entry in the leaderboard list.
 *
 * The rank badge uses gold / silver / bronze colors for the top 3 positions.
 * All other ranks are shown in a muted color.
 *
 * @param rank     1-based position on the leaderboard.
 * @param item     The quiz history entry to display.
 * @param cardColor Background color of the card.
 * @param textDark  Primary text color.
 * @param textMuted Secondary/muted text color.
 */
@Composable
fun LeaderboardItemCard(
    rank: Int,
    item: QuizHistoryItem,
    cardColor: Color,
    textDark: Color,
    textMuted: Color
) {
    // Top 3 get medal colors; all others use a dimmed muted color
    val rankColor = when (rank) {
        1    -> Gold
        2    -> Silver
        3    -> Bronze
        else -> textMuted.copy(alpha = 0.65f)
    }

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(22.dp),
        colors    = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier          = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank badge — circular with a tinted background matching the medal color
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(rankColor.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = rank.toString(),
                    // Top 3 use the medal color; others use the primary text color
                    color      = if (rank <= 3) rankColor else textDark,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize   = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Topic name and date
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = item.topic,
                    color      = textDark,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text  = item.date,
                    color = textMuted,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Score displayed in green on the right
            Text(
                text       = item.score,
                color      = PrimaryGreen,
                style      = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

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
    showSystemUi   = true,
    widthDp        = 900,
    heightDp       = 420
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