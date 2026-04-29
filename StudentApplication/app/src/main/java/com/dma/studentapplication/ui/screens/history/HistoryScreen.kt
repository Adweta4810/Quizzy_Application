package com.dma.studentapplication.ui.screens.history

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
private val PrimaryGreenAlpha = Color(0x1A27D17F)
private val DangerRed         = Color(0xFFEF4444)
private val DangerRedAlpha    = Color(0x1AEF4444)

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

private data class TopicMeta(
    val icon: ImageVector,
    val tint: Color,
    val lightBg: Color,
    val darkBg: Color
)

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

data class QuizHistoryItem(
    val id    : Int,
    val topic : String,
    val date  : String,
    val score : String
)

// ── Main screen ───────────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HistoryScreen(
    isDark             : Boolean = isSystemInDarkTheme(),
    historyItems       : List<QuizHistoryItem> = emptyList(),
    onBackHome         : () -> Unit = {},
    onTopicsClick      : () -> Unit = {},
    onProfileClick     : () -> Unit = {},
    onHistoryItemClick : (QuizHistoryItem) -> Unit = {},
    onClearHistory     : () -> Unit = {}          // ← new callback
) {
    val configuration = LocalConfiguration.current
    val isLandscape   = configuration.screenWidthDp > configuration.screenHeightDp

    val backgroundColor = if (isDark) DarkBackground else LightBackground
    val cardColor       = if (isDark) DarkCard       else LightCard
    val navBarColor     = if (isDark) DarkNavBar     else LightNavBar
    val textDark        = if (isDark) DarkTextDark   else LightTextDark
    val textMuted       = if (isDark) DarkTextMuted  else LightTextMuted

    val availableTopics = historyItems.map { it.topic }.distinct().sorted()
    val filters         = listOf("All") + availableTopics
    var selectedFilter  by remember { mutableStateOf("All") }

    if (selectedFilter != "All" && selectedFilter !in availableTopics) {
        selectedFilter = "All"
    }

    val filteredHistory =
        if (selectedFilter == "All") historyItems
        else historyItems.filter { it.topic == selectedFilter }

    // ── Clear history confirmation dialog ─────────────────────────────────────
    var showClearDialog by remember { mutableStateOf(false) }

    if (showClearDialog) {
        ClearHistoryDialog(
            isDark    = isDark,
            onDismiss = { showClearDialog = false },
            onConfirm = {
                showClearDialog = false
                onClearHistory()
            }
        )
    }

    Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
        if (isLandscape) {
            Row(modifier = Modifier.fillMaxSize()) {
                SideNavBar(
                    isDark         = isDark,
                    navBarColor    = navBarColor,
                    textMuted      = textMuted,
                    onBackHome     = onBackHome,
                    onTopicsClick  = onTopicsClick,
                    onProfileClick = onProfileClick
                )

                HistoryContent(
                    modifier           = Modifier.weight(1f),
                    isLandscape        = true,
                    historyItems       = historyItems,
                    filteredHistory    = filteredHistory,
                    filters            = filters,
                    selectedFilter     = selectedFilter,
                    onFilterChange     = { selectedFilter = it },
                    isDark             = isDark,
                    cardColor          = cardColor,
                    textDark           = textDark,
                    textMuted          = textMuted,
                    onHistoryItemClick = onHistoryItemClick,
                    onClearClick       = { if (historyItems.isNotEmpty()) showClearDialog = true }
                )
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                HistoryContent(
                    modifier           = Modifier.weight(1f),
                    isLandscape        = false,
                    historyItems       = historyItems,
                    filteredHistory    = filteredHistory,
                    filters            = filters,
                    selectedFilter     = selectedFilter,
                    onFilterChange     = { selectedFilter = it },
                    isDark             = isDark,
                    cardColor          = cardColor,
                    textDark           = textDark,
                    textMuted          = textMuted,
                    onHistoryItemClick = onHistoryItemClick,
                    onClearClick       = { if (historyItems.isNotEmpty()) showClearDialog = true }
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

// ── Clear history dialog ──────────────────────────────────────────────────────

/**
 * Confirmation dialog shown before wiping all quiz history.
 * The destructive "Clear All" button is styled in red so the user
 * can't accidentally tap it without noticing.
 */
@Composable
private fun ClearHistoryDialog(
    isDark    : Boolean,
    onDismiss : () -> Unit,
    onConfirm : () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector        = Icons.Default.DeleteForever,
                contentDescription = null,
                tint               = DangerRed,
                modifier           = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text       = "Clear All History?",
                fontWeight = FontWeight.ExtraBold
            )
        },
        text = {
            Text("This will permanently delete all your quiz results. This action cannot be undone.")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors  = ButtonDefaults.buttonColors(
                    containerColor = DangerRed,
                    contentColor   = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector        = Icons.Default.DeleteForever,
                    contentDescription = null,
                    modifier           = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text("Clear All", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", fontWeight = FontWeight.SemiBold)
            }
        }
    )
}

// ── Content ───────────────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HistoryContent(
    modifier           : Modifier,
    isLandscape        : Boolean,
    historyItems       : List<QuizHistoryItem>,
    filteredHistory    : List<QuizHistoryItem>,
    filters            : List<String>,
    selectedFilter     : String,
    onFilterChange     : (String) -> Unit,
    isDark             : Boolean,
    cardColor          : Color,
    textDark           : Color,
    textMuted          : Color,
    onHistoryItemClick : (QuizHistoryItem) -> Unit,
    onClearClick       : () -> Unit
) {
    LazyColumn(
        modifier            = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(if (isLandscape) 14.dp else 20.dp),
        contentPadding      = PaddingValues(
            start  = if (isLandscape) 24.dp else 20.dp,
            end    = if (isLandscape) 28.dp else 20.dp,
            top    = if (isLandscape) 18.dp else 42.dp,
            bottom = 24.dp
        )
    ) {
        // ── Header card with Clear button ─────────────────────────────────────
        item {
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(28.dp),
                colors    = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier          = Modifier.fillMaxWidth().padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // History icon
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(PrimaryGreenAlpha),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector        = Icons.Default.History,
                            contentDescription = null,
                            tint               = PrimaryGreen,
                            modifier           = Modifier.size(28.dp)
                        )
                    }

                    // Title + count
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 14.dp)
                    ) {
                        Text(
                            text       = "Quiz History",
                            style      = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color      = textDark
                        )
                        Text(
                            text  = "${historyItems.size} quiz attempt${if (historyItems.size == 1) "" else "s"} completed",
                            style = MaterialTheme.typography.bodyMedium,
                            color = textMuted
                        )
                    }

                    // ── Clear history button ──────────────────────────────────
                    // Disabled (and visually dimmed) when history is already empty
                    val canClear = historyItems.isNotEmpty()

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(if (canClear) DangerRedAlpha else Color.Transparent)
                            .clickable(enabled = canClear) { onClearClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector        = Icons.Default.DeleteForever,
                            contentDescription = "Clear all history",
                            tint               = if (canClear) DangerRed else textMuted.copy(alpha = 0.4f),
                            modifier           = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }

        // ── Filter label ──────────────────────────────────────────────────────
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector        = Icons.Default.FilterList,
                    contentDescription = null,
                    tint               = PrimaryGreen,
                    modifier           = Modifier.size(20.dp)
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

        // ── Filter chips ──────────────────────────────────────────────────────────────
        if (historyItems.isNotEmpty()) {
            item {
                androidx.compose.foundation.lazy.LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding        = PaddingValues(horizontal = 2.dp)
                ) {
                    items(filters) { filter ->
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

        // ── Empty state ───────────────────────────────────────────────────────
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
                            imageVector        = Icons.Default.History,
                            contentDescription = null,
                            tint               = PrimaryGreen,
                            modifier           = Modifier.size(42.dp)
                        )
                        Text(
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
            // ── History cards ─────────────────────────────────────────────────
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

@Composable
private fun SideNavBar(
    isDark        : Boolean,
    navBarColor   : Color,
    textMuted     : Color,
    onBackHome    : () -> Unit,
    onTopicsClick : () -> Unit,
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

@Composable
private fun SideNavItem(
    icon     : ImageVector,
    label    : String,
    selected : Boolean,
    textMuted: Color,
    onClick  : () -> Unit
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

@Composable
private fun BottomNavBar(
    isDark        : Boolean,
    navBarColor   : Color,
    textMuted     : Color,
    onBackHome    : () -> Unit,
    onTopicsClick : () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar(containerColor = navBarColor, tonalElevation = 0.dp) {
        NavigationBarItem(selected = false, onClick = onBackHome,
            icon = { Icon(Icons.Default.Home,    null) }, label = { Text("Home") },    colors = navColors(textMuted))
        NavigationBarItem(selected = false, onClick = onTopicsClick,
            icon = { Icon(Icons.Default.GridView,null) }, label = { Text("Topics") },  colors = navColors(textMuted))
        NavigationBarItem(selected = true,  onClick = {},
            icon = { Icon(Icons.Default.History, null) }, label = { Text("History") }, colors = navColors(textMuted))
        NavigationBarItem(selected = false, onClick = onProfileClick,
            icon = { Icon(Icons.Default.Person,  null) }, label = { Text("Profile") }, colors = navColors(textMuted))
    }
}

@Composable
private fun navColors(textMuted: Color) = NavigationBarItemDefaults.colors(
    selectedIconColor   = PrimaryGreen,
    selectedTextColor   = PrimaryGreen,
    unselectedIconColor = textMuted,
    unselectedTextColor = textMuted,
    indicatorColor      = PrimaryGreenAlpha
)

// ── History card ──────────────────────────────────────────────────────────────

@Composable
fun HistoryCard(
    historyItem : QuizHistoryItem,
    isDark      : Boolean,
    cardColor   : Color,
    textDark    : Color,
    textMuted   : Color,
    onClick     : () -> Unit
) {
    val meta = topicMeta[historyItem.topic]

    Card(
        modifier  = Modifier.fillMaxWidth().clickable { onClick() },
        shape     = RoundedCornerShape(24.dp),
        colors    = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier          = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(if (meta != null) (if (isDark) meta.darkBg else meta.lightBg) else PrimaryGreenAlpha),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = meta?.icon ?: Icons.Default.History,
                    contentDescription = historyItem.topic,
                    tint               = meta?.tint ?: PrimaryGreen,
                    modifier           = Modifier.size(27.dp)
                )
            }

            Column(modifier = Modifier.weight(1f).padding(start = 14.dp)) {
                Text(historyItem.topic,  color = textDark,  style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                Text(historyItem.date,   color = textMuted, style = MaterialTheme.typography.bodySmall,   modifier = Modifier.padding(top = 3.dp))
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(historyItem.score, color = PrimaryGreen, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                Icon(
                    imageVector        = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Open",
                    tint               = textMuted,
                    modifier           = Modifier.padding(top = 4.dp).size(14.dp)
                )
            }
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

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
    StudentApplicationTheme { HistoryScreen(isDark = false, historyItems = previewItems) }
}

@Preview(showBackground = true, showSystemUi = true, widthDp = 900, heightDp = 420, name = "Landscape")
@Composable
fun HistoryScreenLandscapePreview() {
    StudentApplicationTheme { HistoryScreen(isDark = false, historyItems = previewItems) }
}