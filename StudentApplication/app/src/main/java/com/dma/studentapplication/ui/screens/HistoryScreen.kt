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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SportsBasketball
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

// ─── Color tokens (mirrors HomeScreen exactly) ────────────────────────────────

private val PrimaryGreen      = Color(0xFF27D17F)   // HomeScreen selectedNav
private val PrimaryGreenAlpha = Color(0x1A27D17F)

// Light
private val LightBackground  = Color(0xFFF3F4F6)    // HomeScreen screenBg light
private val LightCard        = Color(0xFFFFFFFF)    // HomeScreen surfaceColor light
private val LightBorder      = Color(0xFFE6ECF2)    // HomeScreen cardBorder light
private val LightTextDark    = Color(0xFF0B1B4A)    // HomeScreen titleColor light
private val LightTextMuted   = Color(0xFF5B6785)    // HomeScreen subtitleColor light
private val LightNavBar      = Color(0xFFFFFFFF)    // HomeScreen bottomBarColor light

// Dark
private val DarkBackground   = Color(0xFF000B1B)    // HomeScreen screenBg dark
private val DarkCard         = Color(0xFF071833)    // HomeScreen surfaceColor dark
private val DarkBorder       = Color(0xFF1A2A40)    // HomeScreen cardBorder dark (White 8%)
private val DarkTextDark     = Color(0xFFFFFFFF)    // HomeScreen titleColor dark
private val DarkTextMuted    = Color(0xFF8FA3C8)    // HomeScreen unselectedNav dark
private val DarkNavBar       = Color(0xFF041225)    // HomeScreen bottomBarColor dark

// ─── Topic icon meta (same icons/tints as HomeScreen topics list) ─────────────

private data class TopicMeta(val icon: ImageVector, val tint: Color, val lightBg: Color, val darkBg: Color)

private val topicMeta: Map<String, TopicMeta> = mapOf(
    "Math"        to TopicMeta(Icons.Default.Calculate,        Color(0xFF22C55E), Color(0xFFEAF8EE), Color(0xFF0C2540)),
    "History"     to TopicMeta(Icons.Outlined.AutoStories,     Color(0xFF4B83FF), Color(0xFFEEF4FF), Color(0xFF10284A)),
    "Science"     to TopicMeta(Icons.Default.Science,          Color(0xFF8B5CF6), Color(0xFFF3EDFF), Color(0xFF1A234B)),
    "Programming" to TopicMeta(Icons.Default.Code,             Color(0xFF10CBE8), Color(0xFFEAFBFF), Color(0xFF102848)),
    "Movies"      to TopicMeta(Icons.Default.LocalMovies,      Color(0xFFF45B87), Color(0xFFFFEEF3), Color(0xFF2A1C3F)),
    "Sports"      to TopicMeta(Icons.Default.SportsBasketball, Color(0xFFF59E0B), Color(0xFFFFF5E6), Color(0xFF3A2810)),
    "Geography"   to TopicMeta(Icons.Default.Public,           Color(0xFF14B87A), Color(0xFFEAFBF5), Color(0xFF102A2B)),
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
    onBackHome: () -> Unit = {},
    onTopicsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onHistoryItemClick: (QuizHistoryItem) -> Unit = {}
) {
    // Resolved colors — exact same derivation logic as HomeScreen
    val backgroundColor = if (isDark) DarkBackground else LightBackground
    val cardColor       = if (isDark) DarkCard       else LightCard
    val borderColor     = if (isDark) DarkBorder     else LightBorder
    val navBarColor     = if (isDark) DarkNavBar     else LightNavBar
    val textDark        = if (isDark) DarkTextDark   else LightTextDark
    val textMuted       = if (isDark) DarkTextMuted  else LightTextMuted

    val allHistory = listOf(
        QuizHistoryItem(1, "Math",        "23 Apr 2026", "8/10"),
        QuizHistoryItem(2, "History",     "22 Apr 2026", "6/10"),
        QuizHistoryItem(3, "Science",     "21 Apr 2026", "9/10"),
        QuizHistoryItem(4, "Programming", "20 Apr 2026", "7/10"),
        QuizHistoryItem(5, "Math",        "19 Apr 2026", "10/10"),
        QuizHistoryItem(6, "Science",     "18 Apr 2026", "5/10"),
    )

    val filters = listOf("All", "Math", "History", "Science", "Programming")
    var selectedFilter by remember { mutableStateOf("All") }

    val filteredHistory = if (selectedFilter == "All") allHistory
    else allHistory.filter { it.topic == selectedFilter }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = backgroundColor
    ) {
        Column(
            modifier            = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ── Scrollable body ─────────────────────────────────────────────
            LazyColumn(
                modifier            = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header — same style as HomeScreen HeaderSection text
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 32.dp, bottom = 4.dp)
                    ) {
                        Text(
                            text       = "Quiz History",
                            style      = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,       // ExtraBold like HomeScreen greeting
                            color      = textDark
                        )
                        Text(
                            text     = "See your previous quiz attempts",
                            style    = MaterialTheme.typography.bodyMedium,
                            color    = textMuted,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                // Filter label — mirrors HomeScreen SectionTitle style
                item {
                    Row(
                        modifier          = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter",
                            tint     = PrimaryGreen,               // HomeScreen selectedNav green
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text       = "Filter by topic",
                            color      = textDark,
                            style      = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,     // ExtraBold like SectionTitle
                            modifier   = Modifier.padding(start = 10.dp)
                        )
                    }
                }

                // Filter chips
                item {
                    FlowRow(
                        modifier                = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        horizontalArrangement   = Arrangement.spacedBy(8.dp),
                        verticalArrangement     = Arrangement.spacedBy(4.dp)
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

                // Empty state
                if (filteredHistory.isEmpty()) {
                    item {
                        Box(
                            modifier        = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text  = "No quiz history found",
                                color = textMuted,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                } else {
                    items(filteredHistory) { item ->
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

            // ── Bottom nav — identical token usage to HomeScreen BottomBar ──
            NavigationBar(
                containerColor = navBarColor,
                tonalElevation = 0.dp
            ) {
                listOf(
                    Triple(Icons.Default.Home,    "Home",    false) to onBackHome,
                    Triple(Icons.Default.GridView, "Topics", false) to onTopicsClick,
                    Triple(Icons.Default.History, "History", true)  to ({}),
                    Triple(Icons.Default.Person,  "Profile", false) to onProfileClick,
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
                            indicatorColor      = Color.Transparent   // no pill — matches HomeScreen
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
    // Look up topic icon/colors — falls back to a green circle if unknown topic
    val meta = topicMeta[historyItem.topic]

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { onClick() },
        shape     = RoundedCornerShape(24.dp),          // matches TopicProgressCard radius
        colors    = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier          = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Topic icon bubble — same construction as TopicProgressCard in HomeScreen
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        if (meta != null)
                            if (isDark) meta.darkBg else meta.lightBg
                        else
                            PrimaryGreenAlpha
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (meta != null) {
                    Icon(
                        imageVector        = meta.icon,
                        contentDescription = historyItem.topic,
                        tint               = meta.tint,
                        modifier           = Modifier.size(26.dp)
                    )
                }
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
                    fontWeight = FontWeight.ExtraBold       // matches HomeScreen card titles
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
                    color      = PrimaryGreen,              // accent matches HomeScreen green
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

@Preview(showBackground = true, showSystemUi = true, name = "Light Theme")
@Composable
fun HistoryScreenLightPreview() {
    StudentApplicationTheme { HistoryScreen(isDark = false) }
}

@Preview(showBackground = true, showSystemUi = true, name = "Dark Theme")
@Composable
fun HistoryScreenDarkPreview() {
    StudentApplicationTheme { HistoryScreen(isDark = true) }
}