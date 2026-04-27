package com.dma.studentapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FilterList
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
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dma.studentapplication.ui.theme.StudentApplicationTheme

// ─── Color tokens ─────────────────────────────────────────────────────────────

private val PrimaryGreen      = Color(0xFF27D17F)
private val PrimaryGreenAlpha = Color(0x1A27D17F)

private val LightBackground  = Color(0xFFF3F4F6)
private val LightCard        = Color(0xFFFFFFFF)
private val LightBorder      = Color(0xFFE6ECF2)
private val LightTextDark    = Color(0xFF0B1B4A)
private val LightTextMuted   = Color(0xFF5B6785)
private val LightNavBar      = Color(0xFFFFFFFF)

private val DarkBackground   = Color(0xFF000B1B)
private val DarkCard         = Color(0xFF071833)
private val DarkBorder       = Color(0xFF1A2A40)
private val DarkTextDark     = Color(0xFFFFFFFF)
private val DarkTextMuted    = Color(0xFF8FA3C8)
private val DarkNavBar       = Color(0xFF041225)

// ─── Topic icon meta ──────────────────────────────────────────────────────────

private data class TopicMeta(
    val icon: ImageVector,
    val tint: Color,
    val lightBg: Color,
    val darkBg: Color
)

private val topicMeta: Map<String, TopicMeta> = mapOf(
    "Math"           to TopicMeta(Icons.Default.Calculate,        Color(0xFF22C55E), Color(0xFFEAF8EE), Color(0xFF0C2540)),
    "Mathematics"    to TopicMeta(Icons.Default.Calculate,        Color(0xFF22C55E), Color(0xFFEAF8EE), Color(0xFF0C2540)),
    "History"        to TopicMeta(Icons.Outlined.AutoStories,     Color(0xFF4B83FF), Color(0xFFEEF4FF), Color(0xFF10284A)),
    "Science"        to TopicMeta(Icons.Default.Science,          Color(0xFF8B5CF6), Color(0xFFF3EDFF), Color(0xFF1A234B)),
    "Programming"    to TopicMeta(Icons.Default.Code,             Color(0xFF10CBE8), Color(0xFFEAFBFF), Color(0xFF102848)),
    "Movies"         to TopicMeta(Icons.Default.LocalMovies,      Color(0xFFF45B87), Color(0xFFFFEEF3), Color(0xFF2A1C3F)),
    "Sports"         to TopicMeta(Icons.Default.SportsBasketball, Color(0xFFF59E0B), Color(0xFFFFF5E6), Color(0xFF3A2810)),
    "Geography"      to TopicMeta(Icons.Default.Public,           Color(0xFF14B87A), Color(0xFFEAFBF5), Color(0xFF102A2B)),
    "Networking"     to TopicMeta(Icons.Default.Router,           Color(0xFF0EA5E9), Color(0xFFE0F2FE), Color(0xFF0C1A2E)),
    "Technology"     to TopicMeta(Icons.Default.Devices,          Color(0xFF6366F1), Color(0xFFE0E7FF), Color(0xFF12123A)),
    "Current Affairs" to TopicMeta(Icons.Default.Newspaper,       Color(0xFFEF4444), Color(0xFFFEE2E2), Color(0xFF2A0E0E)),
    "Daily Quiz"     to TopicMeta(Icons.Default.Devices,          Color(0xFF6366F1), Color(0xFFE0E7FF), Color(0xFF12123A)),
)

// ─── Data model ───────────────────────────────────────────────────────────────

data class QuizHistoryItem(
    val id: Int,
    val topic: String,
    val date: String,
    val score: String
)

// ─── Screen ───────────────────────────────────────────────────────────────────

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
    val backgroundColor = if (isDark) DarkBackground else LightBackground
    val cardColor       = if (isDark) DarkCard       else LightCard
    val borderColor     = if (isDark) DarkBorder     else LightBorder
    val navBarColor     = if (isDark) DarkNavBar     else LightNavBar
    val textDark        = if (isDark) DarkTextDark   else LightTextDark
    val textMuted       = if (isDark) DarkTextMuted  else LightTextMuted

    // Build filter list dynamically from actual data + "All"
    val availableTopics = historyItems.map { it.topic }.distinct().sorted()
    val filters         = listOf("All") + availableTopics

    var selectedFilter by remember { mutableStateOf("All") }

    // Reset filter if it no longer exists in current data
    if (selectedFilter != "All" && selectedFilter !in availableTopics) {
        selectedFilter = "All"
    }

    val filteredHistory = if (selectedFilter == "All") historyItems
    else historyItems.filter { it.topic == selectedFilter }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = backgroundColor
    ) {
        Column(
            modifier            = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier            = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Header
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 20.dp, top = 45.dp, bottom = 4.dp)
                    ) {
                        Text(
                            text       = "Quiz History",
                            style      = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color      = textDark
                        )
                        Text(
                            text     = "${historyItems.size} quiz attempt${if (historyItems.size == 1) "" else "s"}",
                            style    = MaterialTheme.typography.bodyMedium,
                            color    = textMuted,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                // Filter label
                item {
                    Row(
                        modifier          = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector        = Icons.Default.FilterList,
                            contentDescription = "Filter",
                            tint               = PrimaryGreen,
                            modifier           = Modifier.size(20.dp)
                        )
                        Text(
                            text       = "Filter by topic",
                            color      = textDark,
                            style      = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            modifier   = Modifier.padding(start = 10.dp)
                        )
                    }
                }

                // Filter chips — only shown when there is data
                if (historyItems.isNotEmpty()) {
                    item {
                        FlowRow(
                            modifier              = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement   = Arrangement.spacedBy(4.dp)
                        ) {
                            filters.forEach { filter ->
                                FilterChip(
                                    selected = selectedFilter == filter,
                                    onClick  = { selectedFilter = filter },
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
                                        borderColor         = if (isDark) DarkBorder else Color(0xFFDDDFE5)
                                    )
                                )
                            }
                        }
                    }
                }

                // Empty state
                if (filteredHistory.isEmpty()) {
                    item {
                        Box(
                            modifier         = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text  = if (historyItems.isEmpty()) "No quizzes taken yet" else "No results for this topic",
                                    color = textMuted,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text     = if (historyItems.isEmpty()) "Complete a quiz to see your history here" else "Try a different filter",
                                    color    = textMuted.copy(alpha = 0.6f),
                                    style    = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 6.dp)
                                )
                            }
                        }
                    }
                } else {
                    items(filteredHistory, key = { it.id }) { item ->
                        HistoryCard(
                            historyItem = item,
                            isDark      = isDark,
                            cardColor   = cardColor,
                            borderColor = borderColor,
                            textDark    = textDark,
                            textMuted   = textMuted,
                            onClick     = { onHistoryItemClick(item) }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
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
                    Triple(Icons.Default.History,  "History", true)  to ({}),
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

// ─── History card ─────────────────────────────────────────────────────────────

@Composable
fun HistoryCard(
    historyItem: QuizHistoryItem,
    isDark: Boolean,
    cardColor: Color,
    borderColor: Color,
    textDark: Color,
    textMuted: Color,
    onClick: () -> Unit
) {
    val meta = topicMeta[historyItem.topic]

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { onClick() },
        shape     = RoundedCornerShape(24.dp),
        colors    = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Topic icon bubble
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        if (meta != null)
                            if (isDark) meta.darkBg else meta.lightBg
                        else PrimaryGreenAlpha
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = meta?.icon ?: Icons.Default.History,
                    contentDescription = historyItem.topic,
                    tint               = meta?.tint ?: PrimaryGreen,
                    modifier           = Modifier.size(26.dp)
                )
            }

            // Topic name + date
            Column(
                modifier            = Modifier.weight(1f).padding(start = 14.dp),
                verticalArrangement = Arrangement.Center
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

            // Score + arrow
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text       = historyItem.score,
                    color      = PrimaryGreen,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold
                )
                Icon(
                    imageVector        = Icons.Default.ArrowForwardIos,
                    contentDescription = "Open",
                    tint               = textMuted,
                    modifier           = Modifier.padding(top = 4.dp).size(14.dp)
                )
            }
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

private val previewItems = listOf(
    QuizHistoryItem(1, "Math",        "23 Apr 2026", "8/10"),
    QuizHistoryItem(2, "History",     "22 Apr 2026", "6/10"),
    QuizHistoryItem(3, "Science",     "21 Apr 2026", "9/10"),
    QuizHistoryItem(4, "Programming", "20 Apr 2026", "7/10"),
    QuizHistoryItem(5, "Technology",  "19 Apr 2026", "10/10"),
    QuizHistoryItem(6, "Networking",  "18 Apr 2026", "5/10"),
)

@Preview(showBackground = true, showSystemUi = true, name = "Light Theme")
@Composable
fun HistoryScreenLightPreview() {
    StudentApplicationTheme { HistoryScreen(isDark = false, historyItems = previewItems) }
}

@Preview(showBackground = true, showSystemUi = true, name = "Dark Theme")
@Composable
fun HistoryScreenDarkPreview() {
    StudentApplicationTheme { HistoryScreen(isDark = true, historyItems = previewItems) }
}