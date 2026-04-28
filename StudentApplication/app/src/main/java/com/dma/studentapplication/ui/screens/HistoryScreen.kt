package com.dma.studentapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

// ── Theme colors ──────────────────────────────────────────────────────────────

private val PrimaryGreen      = Color(0xFF27D17F)
private val PrimaryGreenAlpha = Color(0x1A27D17F) // 10% opacity green for icon backgrounds

// Light mode
private val LightBackground = Color(0xFFF3F4F6)
private val LightCard       = Color.White
private val LightTextDark   = Color(0xFF0B1B4A)
private val LightTextMuted  = Color(0xFF5B6785)
private val LightNavBar     = Color.White

// Dark mode
private val DarkBackground = Color(0xFF000B1B)
private val DarkCard       = Color(0xFF071833)
private val DarkTextDark   = Color.White
private val DarkTextMuted  = Color(0xFF8FA3C8)
private val DarkNavBar     = Color(0xFF041225)

// ── Topic metadata ────────────────────────────────────────────────────────────

/**
 * Visual metadata for a quiz topic — icon, tint color, and light/dark backgrounds.
 * Used to render the correct colored icon in each history card.
 */
private data class TopicMeta(
    val icon: ImageVector,
    val tint: Color,
    val lightBg: Color,
    val darkBg: Color
)

/**
 * Maps topic display names to their [TopicMeta].
 * Falls back to a generic history icon if a topic name is not found here.
 */
private val topicMeta = mapOf(
    "Math"            to TopicMeta(Icons.Default.Calculate,        Color(0xFF22C55E), Color(0xFFEAF8EE), Color(0xFF0C2540)),
    "Mathematics"     to TopicMeta(Icons.Default.Calculate,        Color(0xFF22C55E), Color(0xFFEAF8EE), Color(0xFF0C2540)),
    "History"         to TopicMeta(Icons.Outlined.AutoStories,     Color(0xFF4B83FF), Color(0xFFEEF4FF), Color(0xFF10284A)),
    "Science"         to TopicMeta(Icons.Default.Science,          Color(0xFF8B5CF6), Color(0xFFF3EDFF), Color(0xFF1A234B)),
    "Programming"     to TopicMeta(Icons.Default.Code,             Color(0xFF10CBE8), Color(0xFFEAFBFF), Color(0xFF102848)),
    "Movies"          to TopicMeta(Icons.Default.LocalMovies,      Color(0xFFF45B87), Color(0xFFFFEEF3), Color(0xFF2A1C3F)),
    "Sports"          to TopicMeta(Icons.Default.SportsBasketball, Color(0xFFF59E0B), Color(0xFFFFF5E6), Color(0xFF3A2810)),
    "Geography"       to TopicMeta(Icons.Default.Public,           Color(0xFF14B87A), Color(0xFFEAFBF5), Color(0xFF102A2B)),
    "Networking"      to TopicMeta(Icons.Default.Router,           Color(0xFF0EA5E9), Color(0xFFE0F2FE), Color(0xFF0C1A2E)),
    "Technology"      to TopicMeta(Icons.Default.Devices,          Color(0xFF6366F1), Color(0xFFE0E7FF), Color(0xFF12123A)),
    "Current Affairs" to TopicMeta(Icons.Default.Newspaper,        Color(0xFFEF4444), Color(0xFFFEE2E2), Color(0xFF2A0E0E))
)

// ── Data model ────────────────────────────────────────────────────────────────

/**
 * Represents a single completed quiz entry shown in the history list.
 *
 * @param id    Unique identifier used as the lazy list key.
 * @param topic Display name of the quiz topic.
 * @param date  Human-readable date the quiz was completed.
 * @param score Score string in "correct/total" format (e.g. "8/10").
 */
data class QuizHistoryItem(
    val id: Int,
    val topic: String,
    val date: String,
    val score: String
)

// ── Main screen ───────────────────────────────────────────────────────────────

/**
 * Displays the full quiz history screen with topic filter chips and a scrollable list.
 *
 * Adapts layout for portrait (bottom nav bar) and landscape (side nav rail) orientations.
 *
 * @param isDark              Whether dark mode is active. Defaults to system setting.
 * @param historyItems        Full list of completed quiz results to display.
 * @param onBackHome          Called when the Home nav item is tapped.
 * @param onTopicsClick       Called when the Topics nav item is tapped.
 * @param onProfileClick      Called when the Profile nav item is tapped.
 * @param onHistoryItemClick  Called when the user taps a specific history card.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HistoryScreen(
    isDark: Boolean = isSystemInDarkTheme(),
    historyItems: List<QuizHistoryItem> = emptyList(),
    onBackHome: () -> Unit = {},
    onTopicsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onHistoryItemClick: (QuizHistoryItem) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val isLandscape   = configuration.screenWidthDp > configuration.screenHeightDp

    // ── Theme-aware colors ────────────────────────────────────────────────────
    val backgroundColor = if (isDark) DarkBackground else LightBackground
    val cardColor       = if (isDark) DarkCard       else LightCard
    val navBarColor     = if (isDark) DarkNavBar     else LightNavBar
    val textDark        = if (isDark) DarkTextDark   else LightTextDark
    val textMuted       = if (isDark) DarkTextMuted  else LightTextMuted

    // ── Filter state ──────────────────────────────────────────────────────────
    // Build filter list from distinct topics found in history, sorted alphabetically
    val availableTopics = historyItems.map { it.topic }.distinct().sorted()
    val filters         = listOf("All") + availableTopics
    var selectedFilter  by remember { mutableStateOf("All") }

    // Reset filter to "All" if the previously selected topic no longer exists in history
    if (selectedFilter != "All" && selectedFilter !in availableTopics) {
        selectedFilter = "All"
    }

    // Apply the active filter — show all items or only those matching the selected topic
    val filteredHistory =
        if (selectedFilter == "All") historyItems
        else historyItems.filter { it.topic == selectedFilter }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        if (isLandscape) {
            // Landscape: side nav rail on the left, content fills the rest
            Row(modifier = Modifier.fillMaxSize()) {
                SideNavBar(
                    isDark       = isDark,
                    navBarColor  = navBarColor,
                    textMuted    = textMuted,
                    onBackHome   = onBackHome,
                    onTopicsClick  = onTopicsClick,
                    onProfileClick = onProfileClick
                )

                HistoryContent(
                    modifier        = Modifier.weight(1f),
                    isLandscape     = true,
                    historyItems    = historyItems,
                    filteredHistory = filteredHistory,
                    filters         = filters,
                    selectedFilter  = selectedFilter,
                    onFilterChange  = { selectedFilter = it },
                    isDark          = isDark,
                    cardColor       = cardColor,
                    textDark        = textDark,
                    textMuted       = textMuted,
                    onHistoryItemClick = onHistoryItemClick
                )
            }
        } else {
            // Portrait: content on top, bottom nav bar below
            Column(modifier = Modifier.fillMaxSize()) {
                HistoryContent(
                    modifier        = Modifier.weight(1f),
                    isLandscape     = false,
                    historyItems    = historyItems,
                    filteredHistory = filteredHistory,
                    filters         = filters,
                    selectedFilter  = selectedFilter,
                    onFilterChange  = { selectedFilter = it },
                    isDark          = isDark,
                    cardColor       = cardColor,
                    textDark        = textDark,
                    textMuted       = textMuted,
                    onHistoryItemClick = onHistoryItemClick
                )

                BottomNavBar(
                    isDark         = isDark,
                    navBarColor    = navBarColor,
                    textMuted      = textMuted,
                    onBackHome     = onBackHome,
                    onTopicsClick  = onTopicsClick,
                    onProfileClick = onProfileClick
                )
            }
        }
    }
}

// ── Content ───────────────────────────────────────────────────────────────────

/**
 * Scrollable history content area containing:
 * - Header card with total quiz count
 * - Filter label and topic filter chips
 * - Filtered list of history cards (or an empty state card)
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HistoryContent(
    modifier: Modifier,
    isLandscape: Boolean,
    historyItems: List<QuizHistoryItem>,
    filteredHistory: List<QuizHistoryItem>,
    filters: List<String>,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    isDark: Boolean,
    cardColor: Color,
    textDark: Color,
    textMuted: Color,
    onHistoryItemClick: (QuizHistoryItem) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(if (isLandscape) 14.dp else 20.dp),
        contentPadding = PaddingValues(
            start  = if (isLandscape) 24.dp else 20.dp,
            end    = if (isLandscape) 28.dp else 20.dp,
            top    = if (isLandscape) 18.dp else 42.dp,
            bottom = 24.dp
        )
    ) {
        // Header card — history icon + total attempt count
        item {
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(28.dp),
                colors    = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(PrimaryGreenAlpha),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint     = PrimaryGreen,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Column(modifier = Modifier.padding(start = 14.dp)) {
                        Text(
                            text       = "Quiz History",
                            style      = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color      = textDark
                        )
                        // Pluralise "attempt" correctly based on count
                        Text(
                            text  = "${historyItems.size} quiz attempt${if (historyItems.size == 1) "" else "s"} completed",
                            style = MaterialTheme.typography.bodyMedium,
                            color = textMuted
                        )
                    }
                }
            }
        }

        // Filter section label
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = null,
                    tint     = PrimaryGreen,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text       = "Filter by topic",
                    color      = textDark,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.padding(start = 10.dp)
                )
            }
        }

        // Filter chips — only shown when there is at least one history entry
        if (historyItems.isNotEmpty()) {
            item {
                FlowRow(
                    modifier                = Modifier.fillMaxWidth(),
                    horizontalArrangement   = Arrangement.spacedBy(8.dp),
                    verticalArrangement     = Arrangement.spacedBy(6.dp)
                ) {
                    filters.forEach { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick  = { onFilterChange(filter) },
                            label    = { Text(filter) },
                            colors   = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryGreenAlpha,
                                selectedLabelColor     = PrimaryGreen,
                                containerColor         = if (isDark) DarkCard else Color(0xFFF0F1F3),
                                labelColor             = textMuted
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled             = true,
                                selected            = selectedFilter == filter,
                                selectedBorderColor = PrimaryGreen,
                                borderColor         = Color.Transparent
                            )
                        )
                    }
                }
            }
        }

        // Empty state — shown when no history exists or no results match the filter
        if (filteredHistory.isEmpty()) {
            item {
                Card(
                    modifier  = Modifier.fillMaxWidth(),
                    shape     = RoundedCornerShape(28.dp),
                    colors    = CardDefaults.cardColors(containerColor = cardColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 44.dp, horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint     = PrimaryGreen,
                            modifier = Modifier.size(42.dp)
                        )
                        Text(
                            // Different message depending on whether history is empty or just filtered out
                            text       = if (historyItems.isEmpty()) "No quizzes taken yet" else "No results for this topic",
                            color      = textDark,
                            style      = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier   = Modifier.padding(top = 14.dp)
                        )
                        Text(
                            text     = if (historyItems.isEmpty()) "Complete a quiz to see your history here" else "Try another filter",
                            color    = textMuted,
                            style    = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            }
        } else {
            // History cards — one per filtered result, keyed by id for efficient recomposition
            items(filteredHistory, key = { it.id }) { item ->
                HistoryCard(
                    historyItem = item,
                    isDark      = isDark,
                    cardColor   = cardColor,
                    textDark    = textDark,
                    textMuted   = textMuted,
                    onClick     = { onHistoryItemClick(item) }
                )
            }
        }
    }
}

// ── Navigation ────────────────────────────────────────────────────────────────

/**
 * Landscape side navigation rail with Home, Topics, History (selected), and Profile items.
 */
@Composable
private fun SideNavBar(
    isDark: Boolean,
    navBarColor: Color,
    textMuted: Color,
    onBackHome: () -> Unit,
    onTopicsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationRail(
        modifier       = Modifier.fillMaxHeight(),
        containerColor = navBarColor
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        SideNavItem(Icons.Default.Home,    "Home",    false, textMuted, onBackHome)
        SideNavItem(Icons.Default.GridView,"Topics",  false, textMuted, onTopicsClick)
        SideNavItem(Icons.Default.History, "History", true,  textMuted, {})
        SideNavItem(Icons.Default.Person,  "Profile", false, textMuted, onProfileClick)

        Spacer(modifier = Modifier.weight(1f))
    }
}

/** Single item inside the landscape [SideNavBar]. */
@Composable
private fun SideNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    textMuted: Color,
    onClick: () -> Unit
) {
    NavigationRailItem(
        selected = selected,
        onClick  = onClick,
        icon     = { Icon(icon, contentDescription = label) },
        label    = { Text(label) },
        colors   = NavigationRailItemDefaults.colors(
            selectedIconColor   = PrimaryGreen,
            selectedTextColor   = PrimaryGreen,
            unselectedIconColor = textMuted,
            unselectedTextColor = textMuted,
            indicatorColor      = PrimaryGreenAlpha
        )
    )
}

/**
 * Portrait bottom navigation bar with Home, Topics, History (selected), and Profile items.
 */
@Composable
private fun BottomNavBar(
    isDark: Boolean,
    navBarColor: Color,
    textMuted: Color,
    onBackHome: () -> Unit,
    onTopicsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar(
        containerColor = navBarColor,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = false, onClick = onBackHome,
            icon  = { Icon(Icons.Default.Home,    contentDescription = "Home") },
            label = { Text("Home") },
            colors = navColors(textMuted)
        )
        NavigationBarItem(
            selected = false, onClick = onTopicsClick,
            icon  = { Icon(Icons.Default.GridView, contentDescription = "Topics") },
            label = { Text("Topics") },
            colors = navColors(textMuted)
        )
        NavigationBarItem(
            selected = true, onClick = {},           // Already on History
            icon  = { Icon(Icons.Default.History,  contentDescription = "History") },
            label = { Text("History") },
            colors = navColors(textMuted)
        )
        NavigationBarItem(
            selected = false, onClick = onProfileClick,
            icon  = { Icon(Icons.Default.Person,   contentDescription = "Profile") },
            label = { Text("Profile") },
            colors = navColors(textMuted)
        )
    }
}

/**
 * Shared [NavigationBarItemColors] used by every item in [BottomNavBar].
 * Extracts the repetitive color block into one place.
 */
@Composable
private fun navColors(textMuted: Color): NavigationBarItemColors {
    return NavigationBarItemDefaults.colors(
        selectedIconColor   = PrimaryGreen,
        selectedTextColor   = PrimaryGreen,
        unselectedIconColor = textMuted,
        unselectedTextColor = textMuted,
        indicatorColor      = PrimaryGreenAlpha
    )
}

// ── History card ──────────────────────────────────────────────────────────────

/**
 * Card representing a single completed quiz in the history list.
 *
 * Displays the topic icon, topic name, date, score, and a forward arrow.
 * Tapping the card calls [onClick] to open the detail screen.
 *
 * @param historyItem The quiz result to display.
 * @param isDark      Whether dark mode is active (affects icon background color).
 * @param cardColor   Background color of the card surface.
 * @param textDark    Primary text color.
 * @param textMuted   Secondary/muted text color.
 * @param onClick     Called when the card is tapped.
 */
@Composable
fun HistoryCard(
    historyItem: QuizHistoryItem,
    isDark: Boolean,
    cardColor: Color,
    textDark: Color,
    textMuted: Color,
    onClick: () -> Unit
) {
    // Look up topic icon/color metadata; falls back to generic history icon if not found
    val meta = topicMeta[historyItem.topic]

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape     = RoundedCornerShape(24.dp),
        colors    = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Topic icon with theme-aware background
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(
                        if (meta != null) {
                            if (isDark) meta.darkBg else meta.lightBg
                        } else {
                            PrimaryGreenAlpha // Fallback for unknown topics
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = meta?.icon ?: Icons.Default.History,
                    contentDescription = historyItem.topic,
                    tint     = meta?.tint ?: PrimaryGreen,
                    modifier = Modifier.size(27.dp)
                )
            }

            // Topic name and date
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text       = historyItem.topic,
                    color      = textDark,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text     = historyItem.date,
                    color    = textMuted,
                    style    = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }

            // Score and forward arrow aligned to the right
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text       = historyItem.score,
                    color      = PrimaryGreen,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Open",
                    tint     = textMuted,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(14.dp)
                )
            }
        }
    }
}

// ── Preview data & previews ───────────────────────────────────────────────────

private val previewItems = listOf(
    QuizHistoryItem(1, "Math",        "23 Apr 2026", "8/10"),
    QuizHistoryItem(2, "History",     "22 Apr 2026", "6/10"),
    QuizHistoryItem(3, "Science",     "21 Apr 2026", "9/10"),
    QuizHistoryItem(4, "Programming", "20 Apr 2026", "7/10"),
    QuizHistoryItem(5, "Technology",  "19 Apr 2026", "10/10"),
    QuizHistoryItem(6, "Networking",  "18 Apr 2026", "5/10")
)

@Preview(showBackground = true, showSystemUi = true, name = "Portrait")
@Composable
fun HistoryScreenPortraitPreview() {
    StudentApplicationTheme {
        HistoryScreen(isDark = false, historyItems = previewItems)
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    widthDp = 900,
    heightDp = 420,
    name = "Landscape"
)
@Composable
fun HistoryScreenLandscapePreview() {
    StudentApplicationTheme {
        HistoryScreen(isDark = false, historyItems = previewItems)
    }
}