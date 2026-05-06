package com.dma.studentapplication.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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

// ─────────────────────────────────────────────────────────────────────────────
// Color tokens
//
// Defined once at file scope so they never scatter across composable bodies.
// Changing a value here updates every usage automatically.
// ─────────────────────────────────────────────────────────────────────────────

/** Brand accent used for selected states, icons, and score text. */
private val PrimaryGreen      = Color(0xFF27D17F)

/** 10 % opacity tint of [PrimaryGreen] — used for icon container backgrounds. */
private val PrimaryGreenAlpha = Color(0x1A27D17F)

/** Destructive action color used for the clear-history button and dialog. */
private val DangerRed         = Color(0xFFEF4444)

/** 10 % opacity tint of [DangerRed] — used for the delete button background. */
private val DangerRedAlpha    = Color(0x1AEF4444)

// Light theme surface colors
private val LightBackground = Color(0xFFF3F4F6)
private val LightCard       = Color.White
private val LightTextDark   = Color(0xFF0B1B4A)
private val LightTextMuted  = Color(0xFF5B6785)
private val LightNavBar     = Color.White

// Dark theme surface colors
private val DarkBackground = Color(0xFF000B1B)
private val DarkCard       = Color(0xFF071833)
private val DarkTextDark   = Color.White
private val DarkTextMuted  = Color(0xFF8FA3C8)
private val DarkNavBar     = Color(0xFF041225)

// ─────────────────────────────────────────────────────────────────────────────
// Topic metadata
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Visual metadata for a quiz topic — icon, tint color, and light/dark
 * background colors used in both the history card and the filter chips.
 *
 * @property icon     Material icon representing the topic.
 * @property tint     Icon foreground color.
 * @property lightBg  Icon container background in light mode.
 * @property darkBg   Icon container background in dark mode.
 */
private data class TopicMeta(
    val icon   : ImageVector,
    val tint   : Color,
    val lightBg: Color,
    val darkBg : Color
)

/**
 * Lookup map from topic display name → [TopicMeta].
 *
 * Keeping this at file scope means the map is allocated once and shared
 * across all recompositions — no need to wrap it in `remember`.
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

// ─────────────────────────────────────────────────────────────────────────────
// Data model
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Represents a single completed quiz attempt shown in the history list.
 *
 * @property id    Stable unique identifier — used as the LazyColumn item key
 *                 so Compose can animate additions/removals correctly.
 * @property topic Display name of the quiz topic (must match a key in [topicMeta]
 *                 to get a coloured icon; falls back to a generic history icon otherwise).
 * @property date  Human-readable date string, e.g. "23 Apr 2026".
 * @property score Result string shown to the user, e.g. "8/10".
 */
data class QuizHistoryItem(
    val id   : Int,
    val topic: String,
    val date : String,
    val score: String
)

// ─────────────────────────────────────────────────────────────────────────────
// Main screen
// ─────────────────────────────────────────────────────────────────────────────

/**
 * History screen — shows all completed quiz attempts with topic filtering
 * and a destructive "clear all" action.
 *
 * Layout adapts automatically:
 * - **Portrait** → scrollable content + bottom navigation bar.
 * - **Landscape** → side navigation rail + scrollable content side by side.
 *
 * @param isDark             Whether to use the dark color palette. Defaults to
 *                           the system dark-mode setting.
 * @param historyItems       Full unfiltered list of quiz attempts to display.
 * @param onBackHome         Called when the Home nav item is tapped.
 * @param onTopicsClick      Called when the Topics nav item is tapped.
 * @param onProfileClick     Called when the Profile nav item is tapped.
 * @param onHistoryItemClick Called with the tapped [QuizHistoryItem] so the
 *                           caller can navigate to a detail screen.
 * @param onClearHistory     Called after the user confirms "Clear All" in the
 *                           confirmation dialog.
 */
@Composable
fun HistoryScreen(
    isDark             : Boolean = isSystemInDarkTheme(),
    historyItems       : List<QuizHistoryItem> = emptyList(),
    onBackHome         : () -> Unit = {},
    onTopicsClick      : () -> Unit = {},
    onProfileClick     : () -> Unit = {},
    onHistoryItemClick : (QuizHistoryItem) -> Unit = {},
    onClearHistory     : () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val isLandscape   = configuration.screenWidthDp > configuration.screenHeightDp

    // Resolve theme-aware colors once so child composables don't need isDark
    val backgroundColor = if (isDark) DarkBackground else LightBackground
    val cardColor       = if (isDark) DarkCard       else LightCard
    val navBarColor     = if (isDark) DarkNavBar     else LightNavBar
    val textDark        = if (isDark) DarkTextDark   else LightTextDark
    val textMuted       = if (isDark) DarkTextMuted  else LightTextMuted

    // Build filter list: "All" + each distinct topic in alphabetical order
    val availableTopics = historyItems.map { it.topic }.distinct().sorted()
    val filters         = listOf("All") + availableTopics
    var selectedFilter  by remember { mutableStateOf("All") }

    // Reset filter to "All" if the currently selected topic was removed
    // (e.g. after the user clears history and a topic no longer exists)
    if (selectedFilter != "All" && selectedFilter !in availableTopics) {
        selectedFilter = "All"
    }

    val filteredHistory =
        if (selectedFilter == "All") historyItems
        else historyItems.filter { it.topic == selectedFilter }

    // Controls visibility of the "Clear All History?" confirmation dialog
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
            // Landscape: side rail on the left, content fills the remainder
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
            // Portrait: content fills the screen, bottom nav sits below
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

// ─────────────────────────────────────────────────────────────────────────────
// Clear history dialog
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Modal confirmation dialog shown before wiping all quiz history.
 *
 * The destructive "Clear All" button is styled in [DangerRed] so the user
 * cannot accidentally trigger it without noticing. The action is irreversible,
 * which is stated explicitly in the body text.
 *
 * @param isDark    Passed through for any theme-aware styling inside the dialog.
 * @param onDismiss Called when the user taps "Cancel" or outside the dialog.
 * @param onConfirm Called when the user taps "Clear All" — the caller is
 *                  responsible for deleting the data.
 */
@Composable
private fun ClearHistoryDialog(
    isDark   : Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
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
            Text(text = "Clear All History?", fontWeight = FontWeight.ExtraBold)
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

// ─────────────────────────────────────────────────────────────────────────────
// Scrollable content
// ─────────────────────────────────────────────────────────────────────────────

/**
 * The main scrollable body shared by both portrait and landscape layouts.
 *
 * Renders sections in order:
 * 1. Header card (title, attempt count, clear button)
 * 2. "Filter by topic" label
 * 3. Horizontally scrollable filter chips
 * 4. Either an empty-state card or the list of [HistoryCard]s
 *
 * **Why `Row + horizontalScroll` instead of `LazyRow` for the chips?**
 * A `LazyRow` inside a `LazyColumn` creates unbounded horizontal measure
 * constraints. Compose cannot resolve these reliably — the result is that
 * only the chips visible in the initial viewport are rendered and the rest
 * are silently clipped. A plain [Row] with [horizontalScroll] measures all
 * chips in a single pass and never clips content.
 *
 * @param modifier           Applied to the outer [LazyColumn] — typically
 *                           `Modifier.weight(1f)` to fill remaining space.
 * @param isLandscape        Adjusts spacing and padding for the landscape layout.
 * @param historyItems       Full unfiltered list — used for the attempt count
 *                           and to decide whether the clear button is enabled.
 * @param filteredHistory    Pre-filtered list to display in the card list.
 * @param filters            Ordered list of filter labels: ["All", topic, …].
 * @param selectedFilter     Currently active filter label.
 * @param onFilterChange     Called with the new filter label when a chip is tapped.
 * @param isDark             Drives color selection for chips and cards.
 * @param cardColor          Surface color for all cards on this screen.
 * @param textDark           Primary text color.
 * @param textMuted          Secondary / hint text color.
 * @param onHistoryItemClick Called with the tapped item for detail navigation.
 * @param onClearClick       Called when the trash icon in the header is tapped.
 */
@Composable
private fun HistoryContent(
    modifier          : Modifier,
    isLandscape       : Boolean,
    historyItems      : List<QuizHistoryItem>,
    filteredHistory   : List<QuizHistoryItem>,
    filters           : List<String>,
    selectedFilter    : String,
    onFilterChange    : (String) -> Unit,
    isDark            : Boolean,
    cardColor         : Color,
    textDark          : Color,
    textMuted         : Color,
    onHistoryItemClick: (QuizHistoryItem) -> Unit,
    onClearClick      : () -> Unit
) {
    LazyColumn(
        modifier            = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(if (isLandscape) 14.dp else 16.dp),
        contentPadding      = PaddingValues(
            start  = if (isLandscape) 24.dp else 16.dp,
            end    = if (isLandscape) 28.dp else 16.dp,
            top    = 45.dp,
            bottom = 24.dp
        )
    ) {

        // ── 1. Header card ────────────────────────────────────────────────────
        // Shows the screen title, total attempt count, and the destructive
        // clear button. The clear button is disabled (dimmed) when the list
        // is already empty so the user cannot tap it pointlessly.
        item {
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(28.dp),
                colors    = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // History icon bubble
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

                    // Title and attempt count
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

                    // Clear history button — red when active, dimmed when list is empty
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

        // ── 2. Filter label ───────────────────────────────────────────────────
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
                    modifier   = Modifier.padding(start = 8.dp)
                )
            }
        }

        // ── 3. Filter chips ───────────────────────────────────────────────────
        // Uses Row + horizontalScroll instead of LazyRow to avoid the nested
        // lazy layout bug that silently clips chips outside the initial viewport.
        item {
            Row(
                modifier              = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
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

        // ── 4a. Empty state ───────────────────────────────────────────────────
        // Shown when either no quizzes have been taken at all, or the active
        // filter returns no results. Copy differs between the two cases.
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
                            text       = if (historyItems.isEmpty()) "No quizzes taken yet"
                            else "No results for this topic",
                            color      = textDark,
                            style      = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier   = Modifier.padding(top = 14.dp)
                        )
                        Text(
                            text     = if (historyItems.isEmpty()) "Complete a quiz to see your history here"
                            else "Try another filter",
                            color    = textMuted,
                            style    = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            }
        } else {
            // ── 4b. History card list ─────────────────────────────────────────
            // key = item.id ensures Compose animates insertions/deletions
            // correctly rather than recomposing every card on list change.
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

// ─────────────────────────────────────────────────────────────────────────────
// Navigation
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Landscape side navigation rail with History pre-selected.
 *
 * @param isDark         Unused directly but kept for symmetry with [BottomNavBar]
 *                       in case theme-specific rail styling is added later.
 * @param navBarColor    Container background color.
 * @param textMuted      Unselected item label and icon color.
 * @param onBackHome     Called when Home is tapped.
 * @param onTopicsClick  Called when Topics is tapped.
 * @param onProfileClick Called when Profile is tapped.
 */
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
        SideNavItem(Icons.Default.Home,    "Home",    selected = false, textMuted, onBackHome)
        SideNavItem(Icons.Default.GridView,"Topics",  selected = false, textMuted, onTopicsClick)
        SideNavItem(Icons.Default.History, "History", selected = true,  textMuted, onClick = {})
        SideNavItem(Icons.Default.Person,  "Profile", selected = false, textMuted, onProfileClick)
        Spacer(modifier = Modifier.weight(1f))
    }
}

/**
 * Single item inside [SideNavBar].
 *
 * @param icon     Icon to display.
 * @param label    Accessibility label and visible text below the icon.
 * @param selected Whether this item is currently active.
 * @param textMuted Unselected color applied to icon and label.
 * @param onClick  Click handler.
 */
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

/**
 * Portrait bottom navigation bar with History pre-selected.
 *
 * @param isDark         Unused directly — kept for API symmetry.
 * @param navBarColor    Container background color.
 * @param textMuted      Unselected item color.
 * @param onBackHome     Called when Home is tapped.
 * @param onTopicsClick  Called when Topics is tapped.
 * @param onProfileClick Called when Profile is tapped.
 */
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
        NavigationBarItem(
            selected = false, onClick = onBackHome,
            icon     = { Icon(Icons.Default.Home,    null) },
            label    = { Text("Home") },
            colors   = navColors(textMuted)
        )
        NavigationBarItem(
            selected = false, onClick = onTopicsClick,
            icon     = { Icon(Icons.Default.GridView, null) },
            label    = { Text("Topics") },
            colors   = navColors(textMuted)
        )
        NavigationBarItem(
            selected = true, onClick = {},
            icon     = { Icon(Icons.Default.History,  null) },
            label    = { Text("History") },
            colors   = navColors(textMuted)
        )
        NavigationBarItem(
            selected = false, onClick = onProfileClick,
            icon     = { Icon(Icons.Default.Person,   null) },
            label    = { Text("Profile") },
            colors   = navColors(textMuted)
        )
    }
}

/**
 * Shared [NavigationBarItemColors] used by every item in [BottomNavBar].
 *
 * Extracted to avoid repeating the same color block four times.
 *
 * @param textMuted Unselected icon and label color.
 */
@Composable
private fun navColors(textMuted: Color) = NavigationBarItemDefaults.colors(
    selectedIconColor   = PrimaryGreen,
    selectedTextColor   = PrimaryGreen,
    unselectedIconColor = textMuted,
    unselectedTextColor = textMuted,
    indicatorColor      = PrimaryGreenAlpha
)

// ─────────────────────────────────────────────────────────────────────────────
// History card
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Single row card representing one completed quiz attempt.
 *
 * Layout (left → right):
 * - Circular icon container with topic-specific color from [topicMeta]
 *   (falls back to a generic history icon if the topic is unrecognised).
 * - Topic name + date column.
 * - Score + forward chevron column (right-aligned).
 *
 * @param historyItem The quiz attempt to display.
 * @param isDark      Selects light or dark icon background from [TopicMeta].
 * @param cardColor   Card surface color.
 * @param textDark    Primary text color for topic name.
 * @param textMuted   Secondary text color for date and chevron.
 * @param onClick     Called when the card is tapped.
 */
@Composable
fun HistoryCard(
    historyItem: QuizHistoryItem,
    isDark     : Boolean,
    cardColor  : Color,
    textDark   : Color,
    textMuted  : Color,
    onClick    : () -> Unit
) {
    // Resolve topic visual metadata — null-safe fallback for unknown topics
    val meta = topicMeta[historyItem.topic]

    Card(
        modifier  = Modifier.fillMaxWidth().clickable { onClick() },
        shape     = RoundedCornerShape(24.dp),
        colors    = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Topic icon with theme-aware background color
            Box(
                modifier = Modifier
                    .size(54.dp)
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
                    modifier           = Modifier.size(27.dp)
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

            // Score and navigation chevron (right-aligned)
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text       = historyItem.score,
                    color      = PrimaryGreen,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold
                )
                Icon(
                    imageVector        = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Open",
                    tint               = textMuted,
                    modifier           = Modifier
                        .padding(top = 4.dp)
                        .size(14.dp)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

private val previewItems = listOf(
    QuizHistoryItem(1, "Math",        "23 Apr 2026", "8/10"),
    QuizHistoryItem(2, "History",     "22 Apr 2026", "6/10"),
    QuizHistoryItem(3, "Science",     "21 Apr 2026", "9/10"),
    QuizHistoryItem(4, "Programming", "20 Apr 2026", "7/10"),
    QuizHistoryItem(5, "Technology",  "19 Apr 2026", "10/10"),
    QuizHistoryItem(6, "Networking",  "18 Apr 2026", "5/10")
)

/** Portrait preview — light theme with sample history data. */
@Preview(showBackground = true, showSystemUi = true, name = "Portrait – Light")
@Composable
fun HistoryScreenPortraitPreview() {
    StudentApplicationTheme { HistoryScreen(isDark = false, historyItems = previewItems) }
}

/** Portrait preview — dark theme with sample history data. */
@Preview(showBackground = true, showSystemUi = true, name = "Portrait – Dark")
@Composable
fun HistoryScreenPortraitDarkPreview() {
    StudentApplicationTheme { HistoryScreen(isDark = true, historyItems = previewItems) }
}

/** Landscape preview — light theme with sample history data. */
@Preview(showBackground = true, showSystemUi = true, widthDp = 900, heightDp = 420, name = "Landscape – Light")
@Composable
fun HistoryScreenLandscapePreview() {
    StudentApplicationTheme { HistoryScreen(isDark = false, historyItems = previewItems) }
}

/** Empty state preview — no history items. */
@Preview(showBackground = true, showSystemUi = true, name = "Empty State")
@Composable
fun HistoryScreenEmptyPreview() {
    StudentApplicationTheme { HistoryScreen(isDark = false, historyItems = emptyList()) }
}