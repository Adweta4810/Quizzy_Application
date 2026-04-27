package com.dma.studentapplication.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dma.studentapplication.ui.components.RoboBuddy
import com.dma.studentapplication.ui.screens.model.HomeTopicUi
import com.dma.studentapplication.ui.theme.StudentApplicationTheme
import com.dma.studentapplication.utils.constants.greeting

/**
 * Returns `true` when the device is currently in landscape orientation.
 * Recomposes automatically whenever the user rotates the device.
 */
private val isLandscape: Boolean
    @Composable get() = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

/**
 * Root screen composable for the Home destination.
 *
 * Responsibilities:
 * - Derives all theme colors from the current dark/light mode.
 * - Builds the static topic list (remembered to survive recomposition).
 * - Switches between portrait layout (Box + BottomBar) and
 *   landscape layout (Row + NavigationRail) based on device orientation.
 *
 * @param userName        Display name shown in the greeting header.
 * @param onDailyQuizClick Called when the user taps the Daily Quiz card.
 * @param onTopicClick    Called with the topic id when any topic is tapped.
 * @param onProfileClick  Called when the user taps the avatar/profile button.
 */
@Composable
fun HomeScreen(
    userName: String = "Astrea",
    onDailyQuizClick: () -> Unit = {},
    onTopicClick: (String) -> Unit = {},
    onTopicsClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val isDark    = isSystemInDarkTheme()
    val landscape = isLandscape

    /* --- Theme colors -------------------------------------------------------
       All colors are derived here once so every child composable receives a
       concrete Color value instead of recalculating on every recomposition. */
    val screenBg       = if (isDark) Color(0xFF000B1B) else Color(0xFFF3F4F6)
    val surfaceColor   = if (isDark) Color(0xFF071833) else Color(0xFFFFFFFF)
    val cardBorder     = if (isDark) Color.White.copy(alpha = 0.08f) else Color(0xFFE6ECF2)
    val titleColor     = if (isDark) Color(0xFFFFFFFF) else Color(0xFF0B1B4A)
    val subtitleColor  = if (isDark) Color(0xFFB8C4E0) else Color(0xFF5B6785)
    val bottomBarColor = if (isDark) Color(0xFF041225) else Color(0xFFFFFFFF)
    val selectedNav    = Color(0xFF27D17F)
    val unselectedNav  = if (isDark) Color(0xFF8FA3C8) else Color(0xFF6B7280)

    /* Dark mode uses teal tones; light mode uses brighter greens. */
    val dailyQuizGradient = if (isDark)
        Brush.horizontalGradient(listOf(Color(0xFF14838A), Color(0xFF25D39F)))
    else
        Brush.horizontalGradient(listOf(Color(0xFF22C55E), Color(0xFF4ADE80)))

    /* Wrapped in remember so the list is only created once and survives
       recompositions caused by rotation or theme changes. */
    val topics = remember {
        listOf(
            HomeTopicUi("math",        "Math",        Icons.Default.Calculate,        Color(0xFF22C55E), Color(0xFFEAF8EE), Color(0xFF0C2540), 12),
            HomeTopicUi("history",     "History",     Icons.Outlined.AutoStories,     Color(0xFF4B83FF), Color(0xFFEEF4FF), Color(0xFF10284A), 6),
            HomeTopicUi("science",     "Science",     Icons.Default.Science,          Color(0xFF8B5CF6), Color(0xFFF3EDFF), Color(0xFF1A234B), 20),
            HomeTopicUi("programming", "Programming", Icons.Default.Code,             Color(0xFF10CBE8), Color(0xFFEAFBFF), Color(0xFF102848), 8),
            HomeTopicUi("movies",      "Movies",      Icons.Default.LocalMovies,      Color(0xFFF45B87), Color(0xFFFFEEF3), Color(0xFF2A1C3F), 5),
            HomeTopicUi("sports",      "Sports",      Icons.Default.SportsBasketball, Color(0xFFF59E0B), Color(0xFFFFF5E6), Color(0xFF3A2810), 9),
            HomeTopicUi("geography",   "Geography",   Icons.Default.Public,           Color(0xFF14B87A), Color(0xFFEAFBF5), Color(0xFF102A2B), 7),
            HomeTopicUi("technology",  "Technology",  Icons.Default.Devices,          Color(0xFF6366F1), Color(0xFFE0E7FF), Color(0xFF12123A), 10),
            HomeTopicUi("networking",   "Networking",   Icons.Default.Router,           Color(0xFF0EA5E9), Color(0xFFE0F2FE), Color(0xFF0C1A2E), 10),
            HomeTopicUi("current_affairs", "Current Affairs", Icons.Default.Newspaper, Color(0xFFEF4444), Color(0xFFFEE2E2), Color(0xFF2A0E0E), 10),
        )
    }

    /* --- Layout branch ------------------------------------------------------
       Landscape : NavigationRail on the left + scrollable content on the right.
       Portrait  : scrollable content fills the screen, BottomBar floats above. */
    if (landscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(screenBg)
                // safeDrawing handles the notch/cutout on the LEFT edge in
                // landscape mode as well as the top status bar.
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            LandscapeNavRail(
                containerColor  = bottomBarColor,
                selectedColor   = selectedNav,
                unselectedColor = unselectedNav,
                onTopicsClick   = onTopicsClick,
                onHistoryClick  = onHistoryClick,
                onProfileClick  = onProfileClick
            )

            ContentColumn(
                modifier          = Modifier.weight(1f),
                userName          = userName,
                isDark            = isDark,
                landscape         = true,
                surfaceColor      = surfaceColor,
                cardBorder        = cardBorder,
                titleColor        = titleColor,
                subtitleColor     = subtitleColor,
                dailyQuizGradient = dailyQuizGradient,
                topics            = topics,
                onDailyQuizClick  = onDailyQuizClick,
                onTopicClick      = onTopicClick,
                onProfileClick    = onProfileClick
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(screenBg)
                // In portrait only the navigation bar inset is needed;
                // the status bar is handled by the Activity window flags.
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            ContentColumn(
                modifier          = Modifier.fillMaxSize(),
                userName          = userName,
                isDark            = isDark,
                landscape         = false,
                surfaceColor      = surfaceColor,
                cardBorder        = cardBorder,
                titleColor        = titleColor,
                subtitleColor     = subtitleColor,
                dailyQuizGradient = dailyQuizGradient,
                topics            = topics,
                onDailyQuizClick  = onDailyQuizClick,
                onTopicClick      = onTopicClick,
                onProfileClick    = onProfileClick
            )

            /* BottomBar overlaid at the bottom of the Box.
               ContentColumn's bottom contentPadding (96.dp) ensures list
               items are never hidden behind it. */
            BottomBar(
                modifier        = Modifier.align(Alignment.BottomCenter),
                containerColor  = bottomBarColor,
                selectedColor   = selectedNav,
                unselectedColor = unselectedNav,
                onTopicsClick   = onTopicsClick,
                onHistoryClick  = onHistoryClick,
                onProfileClick  = onProfileClick
            )
        }
    }
}

/**
 * Shared scrollable body rendered by both portrait and landscape layouts.
 *
 * Orientation differences handled here:
 * - **Bottom padding** — portrait needs 96.dp to clear the BottomBar;
 *   landscape uses 16.dp since there is no bottom bar.
 * - **Continue Studying** — landscape renders a 2-column grid to make
 *   better use of the extra horizontal space; portrait uses a plain list.
 *
 * @param modifier          Modifier forwarded from the parent layout.
 * @param userName          Passed through to [HeaderSection].
 * @param isDark            Whether dark mode is active.
 * @param landscape         `true` when the device is in landscape orientation.
 * @param surfaceColor      Card background color.
 * @param cardBorder        Card border color.
 * @param titleColor        Primary text color.
 * @param subtitleColor     Secondary text color.
 * @param dailyQuizGradient Gradient brush for the Daily Quiz card.
 * @param topics            Full list of available topics.
 * @param onDailyQuizClick  Called when the Daily Quiz card is tapped.
 * @param onTopicClick      Called with the topic id when a topic card is tapped.
 * @param onProfileClick    Called when the avatar is tapped.
 */
@Composable
private fun ContentColumn(
    modifier: Modifier,
    userName: String,
    isDark: Boolean,
    landscape: Boolean,
    surfaceColor: Color,
    cardBorder: Color,
    titleColor: Color,
    subtitleColor: Color,
    dailyQuizGradient: Brush,
    topics: List<HomeTopicUi>,
    onDailyQuizClick: () -> Unit,
    onTopicClick: (String) -> Unit,
    onProfileClick: () -> Unit
) {
    LazyColumn(
        modifier            = modifier.padding(horizontal = 25.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding      = PaddingValues(
            top    = 50.dp,
            bottom = if (landscape) 16.dp else 96.dp  // 96.dp clears the BottomBar
        )
    ) {
        // Greeting header with user name and tappable avatar
        item {
            HeaderSection(
                userName       = userName,
                titleColor     = titleColor,
                subtitleColor  = subtitleColor,
                isDark         = isDark,
                onProfileClick = onProfileClick
            )
        }

        // Daily Quiz section
        item { SectionTitle("Practice More",     Icons.Outlined.CheckCircle,            titleColor) }
        item { DailyQuizCard(dailyQuizGradient,  onDailyQuizClick) }

        // Quick Topics horizontal scroll
        item { SectionTitle("Quick Topics",      Icons.Outlined.GridView,               titleColor) }
        item {
            /* LazyRow so only the visible topic chips are composed. */
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(topics) { topic ->
                    QuickTopicCard(
                        topic   = topic,
                        isDark  = isDark,
                        onClick = { onTopicClick(topic.id) }
                    )
                }
            }
        }

        // Continue Studying section
        item { SectionTitle("Continue Studying", Icons.AutoMirrored.Filled.MenuBook,    titleColor) }

        if (landscape) {
            /* Landscape: chunk topics into pairs and render a 2-column grid.
               An invisible Spacer fills the second slot if the count is odd. */
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    topics.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            row.forEach { topic ->
                                TopicProgressCard(
                                    modifier      = Modifier.weight(1f),
                                    topic         = topic,
                                    isDark        = isDark,
                                    surfaceColor  = surfaceColor,
                                    borderColor   = cardBorder,
                                    titleColor    = titleColor,
                                    subtitleColor = subtitleColor,
                                    onClick       = { onTopicClick(topic.id) }
                                )
                            }
                            if (row.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        } else {
            // Portrait: simple vertical list limited to 3 topics
            items(topics) { topic ->
                TopicProgressCard(
                    topic         = topic,
                    isDark        = isDark,
                    surfaceColor  = surfaceColor,
                    borderColor   = cardBorder,
                    titleColor    = titleColor,
                    subtitleColor = subtitleColor,
                    onClick       = { onTopicClick(topic.id) }
                )
            }
        }

        // Summary stats card at the bottom of the list
        item {
            SummaryCard(
                surfaceColor  = surfaceColor,
                borderColor   = cardBorder,
                titleColor    = titleColor,
                subtitleColor = subtitleColor
            )
        }
    }
}

/**
 * Vertical navigation rail displayed on the left side in landscape mode.
 *
 * Replaces [BottomBar] when screen height is limited so all vertical space
 * is available for scrollable content.
 *
 * @param containerColor  Background color of the rail.
 * @param selectedColor   Icon and label color for the selected destination.
 * @param unselectedColor Icon and label color for unselected destinations.
 */
@Composable
private fun LandscapeNavRail(
    containerColor: Color,
    selectedColor: Color,
    unselectedColor: Color,
    onTopicsClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    /* Remembered so the list object is not recreated on recomposition. */
    val navItems = remember {
        listOf(
            Icons.Default.Home      to "Home",
            Icons.Outlined.GridView to "Topics",
            Icons.Default.History   to "History",
            Icons.Default.Person    to "Profile",
        )
    }

    NavigationRail(
        modifier       = Modifier.fillMaxHeight(),
        containerColor = containerColor,
    ) {
        Spacer(Modifier.height(8.dp)) // breathing room below the status bar

        navItems.forEachIndexed { index, (icon, label) ->
            NavigationRailItem(
                selected = index == 0, // Home is always active on this screen
                onClick  = {
                    when (label) {
                        "Topics" -> onTopicsClick()
                        "History" -> onHistoryClick()
                        "Profile" -> onProfileClick()
                    }
                },
                icon     = { Icon(icon, contentDescription = label) },
                label    = { Text(label, fontSize = 12.sp) },
                colors   = NavigationRailItemDefaults.colors(
                    selectedIconColor   = selectedColor,
                    selectedTextColor   = selectedColor,
                    unselectedIconColor = unselectedColor,
                    unselectedTextColor = unselectedColor,
                    indicatorColor      = Color.Transparent // no pill indicator
                )
            )
        }
    }
}

/**
 * Greeting header displayed at the top of the content list.
 *
 * Shows a waving-hand icon, the user's name, a subtitle, and a tappable
 * [RoboBuddy] avatar that navigates to the profile screen.
 *
 * @param userName       Name displayed in the greeting text.
 * @param titleColor     Color for the primary greeting text.
 * @param subtitleColor  Color for the secondary subtitle text.
 * @param isDark         Whether dark mode is active (affects icon backgrounds).
 * @param onProfileClick Called when the avatar is tapped.
 */
@Composable
private fun HeaderSection(
    userName: String,
    titleColor: Color,
    subtitleColor: Color,
    isDark: Boolean,
    onProfileClick: () -> Unit
) {
    Row(
        modifier          = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: waving hand icon + greeting text
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.padding(start = 14.dp)) {
                Text("Hi $userName,",           color = titleColor,    fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                Text("${greeting()}!", color = subtitleColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }

        /* Right: avatar — intentionally larger than its container (100.dp inside
           a 56.dp Box) so the character fills the circle without extra padding. */
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(if (isDark) Color(0xFF193039) else Color(0x1427D17F))
                .clickable { onProfileClick() },
            contentAlignment = Alignment.Center
        ) {
            RoboBuddy(modifier = Modifier.size(100.dp))
        }
    }
}

/**
 * Small section header with a tinted icon and a bold label.
 *
 * Used above each content group (e.g. "Quick Topics", "Continue Studying").
 *
 * @param title      Text displayed next to the icon.
 * @param icon       Icon shown to the left of the title.
 * @param titleColor Color applied to both the icon tint and the text.
 */
@Composable
private fun SectionTitle(title: String, icon: ImageVector, titleColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = title, tint = Color(0xFF27D17F), modifier = Modifier.size(20.dp))
        Text(
            text       = title,
            color      = titleColor,
            fontSize   = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier   = Modifier.padding(start = 10.dp)
        )
    }
}

/**
 * Recent Quiz card using the same gradient as the old Daily Quiz card.
 *
 * Shows a "RECENT QUIZ" label, topic icon, quiz title on the left,
 * and an arc score ring on the right.
 *
 * @param gradient          Horizontal gradient brush applied to the card background.
 * @param onClick           Called when the card is tapped.
 * @param recentTopic       Title of the most recently attempted quiz.
 * @param recentTopicIcon   Icon representing the quiz topic.
 * @param recentScore       Score percentage (0–100) shown in the ring.
 */
@Composable
private fun DailyQuizCard(
    gradient: Brush,
    onClick: () -> Unit,
    recentTopic: String = "A Basic Music Quiz",
    recentTopicIcon: ImageVector = Icons.Default.MusicNote,
    recentScore: Int = 65
) {
    Card(
        modifier  = Modifier.fillMaxWidth().clickable { onClick() },
        shape     = RoundedCornerShape(28.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(horizontal = 18.dp, vertical = 18.dp)
        ) {
            Row(
                modifier          = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: label + icon row + title
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text          = "RECENT QUIZ",
                        color         = Color.White.copy(alpha = 0.80f),
                        fontSize      = 15.sp,
                        fontWeight    = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Icon bubble
                        Box(
                            modifier         = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector        = recentTopicIcon,
                                contentDescription = null,
                                tint               = Color.White,
                                modifier           = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text       = recentTopic,
                            color      = Color.White,
                            fontSize   = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines   = 1,
                            overflow   = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Right: arc score ring
                Box(
                    contentAlignment = Alignment.Center,
                    modifier         = Modifier.size(62.dp)
                ) {
                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = 7.dp.toPx()
                        // Track ring
                        drawArc(
                            color      = Color.White.copy(alpha = 0.25f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter  = false,
                            style      = androidx.compose.ui.graphics.drawscope.Stroke(
                                width = stroke,
                                cap   = androidx.compose.ui.graphics.StrokeCap.Round
                            )
                        )
                        // Progress ring
                        drawArc(
                            color      = Color.White,
                            startAngle = -90f,
                            sweepAngle = 360f * (recentScore / 100f),
                            useCenter  = false,
                            style      = androidx.compose.ui.graphics.drawscope.Stroke(
                                width = stroke,
                                cap   = androidx.compose.ui.graphics.StrokeCap.Round
                            )
                        )
                    }
                    Text(
                        text       = "$recentScore%",
                        color      = Color.White,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

/**
 * Compact card used inside the horizontal Quick Topics [LazyRow].
 *
 * Background color adapts to dark/light mode using the per-topic colors
 * defined in [HomeTopicUi].
 *
 * @param topic   Data model containing the icon, title and theme colors.
 * @param isDark  Whether dark mode is active.
 * @param onClick Called when the card is tapped.
 */
@Composable
private fun QuickTopicCard(topic: HomeTopicUi, isDark: Boolean, onClick: () -> Unit) {
    Card(
        modifier  = Modifier.clickable { onClick() },
        shape     = RoundedCornerShape(22.dp),
        colors    = CardDefaults.cardColors(
            containerColor = if (isDark) topic.darkBg else topic.lightBg
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier            = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(topic.icon, topic.title, tint = topic.iconTint, modifier = Modifier.size(30.dp))
            Text(
                text       = topic.title,
                color      = if (isDark) Color.White else Color(0xFF0F172A),
                fontSize   = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier   = Modifier.padding(top = 10.dp)
            )
        }
    }
}

/**
 * Card showing a topic's icon, name and remaining question count.
 *
 * Used in both portrait (full-width list) and landscape (2-column grid).
 * The optional [modifier] parameter lets the landscape grid pass
 * `Modifier.weight(1f)` so both cards in a row share equal width.
 *
 * @param topic         Data model for the topic.
 * @param isDark        Whether dark mode is active.
 * @param surfaceColor  Card background color.
 * @param borderColor   Card border color.
 * @param titleColor    Color for the topic name text.
 * @param subtitleColor Color for the questions-left text.
 * @param onClick       Called when the card is tapped.
 * @param modifier      Optional modifier; defaults to no extra constraints.
 */
@Composable
private fun TopicProgressCard(
    topic: HomeTopicUi,
    isDark: Boolean,
    surfaceColor: Color,
    borderColor: Color,
    titleColor: Color,
    subtitleColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier.fillMaxWidth().clickable { onClick() },
        shape     = RoundedCornerShape(24.dp),
        colors    = CardDefaults.cardColors(containerColor = surfaceColor),
        border    = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier          = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Topic icon inside a rounded-square badge
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (isDark) topic.darkBg else topic.lightBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(topic.icon, topic.title, tint = topic.iconTint, modifier = Modifier.size(30.dp))
            }

            // Topic name and questions remaining
            Column(modifier = Modifier.weight(1f).padding(start = 14.dp)) {
                Text(
                    text       = topic.title,
                    color      = titleColor,
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis  // graceful truncation for long titles
                )
                Text("${topic.questionsLeft} questions left", color = subtitleColor, fontSize = 14.sp)
            }

            // Forward arrow indicating the card is tappable
            Icon(
                Icons.Default.ArrowForwardIos,
                contentDescription = "Open ${topic.title}",
                tint     = subtitleColor,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

/**
 * Card displaying three quick stats separated by thin vertical dividers.
 *
 * Stats shown: quizzes completed, average accuracy, questions per quiz.
 * Positioned at the bottom of the scrollable content list.
 *
 * @param surfaceColor  Card background color.
 * @param borderColor   Card border and divider color.
 * @param titleColor    Color for the stat value text.
 * @param subtitleColor Color for the stat label text.
 */
@Composable
private fun SummaryCard(surfaceColor: Color, borderColor: Color, titleColor: Color, subtitleColor: Color) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(22.dp),
        colors    = CardDefaults.cardColors(containerColor = surfaceColor),
        border    = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 18.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryItem(Icons.Outlined.CheckCircle, "3",   "Quizzes\nCompleted",  titleColor, subtitleColor, Modifier.weight(1f))
            Divider(modifier = Modifier.size(width = 1.dp, height = 48.dp), color = borderColor)
            SummaryItem(Icons.Default.QueryStats,   "80%", "Average\nAccuracy",   titleColor, subtitleColor, Modifier.weight(1f))
            Divider(modifier = Modifier.size(width = 1.dp, height = 48.dp), color = borderColor)
            SummaryItem(Icons.Default.School,       "10",  "Questions\nPer Quiz", titleColor, subtitleColor, Modifier.weight(1f))
        }
    }
}

/**
 * Single stat cell used inside [SummaryCard].
 *
 * Stacks an icon, a bold numeric value and a two-line label vertically.
 *
 * @param icon          Icon shown above the value.
 * @param value         Bold numeric string (e.g. "80%").
 * @param label         Two-line description below the value.
 * @param titleColor    Color for [value].
 * @param subtitleColor Color for [label].
 * @param modifier      Forwarded from the parent; typically `Modifier.weight(1f)`.
 */
@Composable
private fun SummaryItem(
    icon: ImageVector,
    value: String,
    label: String,
    titleColor: Color,
    subtitleColor: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = Color(0xFF27D17F), modifier = Modifier.size(22.dp))
        Text(value, color = titleColor,    fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(top = 4.dp))
        Text(label, color = subtitleColor, fontSize = 12.sp, lineHeight = 16.sp)
    }
}

/**
 * Material 3 bottom navigation bar shown in portrait mode only.
 *
 * In landscape mode this is replaced by [LandscapeNavRail]. The nav items
 * are defined in the same order as the rail so destinations stay consistent
 * across orientations. The first item (Home) is always marked selected.
 *
 * @param modifier        Modifier forwarded from the parent (used to align to bottom).
 * @param containerColor  Background color of the bar.
 * @param selectedColor   Icon and label color for the selected destination.
 * @param unselectedColor Icon and label color for unselected destinations.
 */
@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    containerColor: Color,
    selectedColor: Color,
    unselectedColor: Color,
    onTopicsClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    /* Remembered so the list object is not recreated on recomposition. */
    val navItems = remember {
        listOf(
            Icons.Default.Home      to "Home",
            Icons.Outlined.GridView to "Topics",
            Icons.Default.History   to "History",
            Icons.Default.Person    to "Profile",
        )
    }

    NavigationBar(
        modifier       = modifier.fillMaxWidth(),
        containerColor = containerColor,
        tonalElevation = 0.dp  // flat — no elevation tint overlay
    ) {
        navItems.forEachIndexed { index, (icon, label) ->
            NavigationBarItem(
                selected = index == 0, // Home is always active on this screen
                onClick  = {
                    when (label) {
                        "Topics" -> onTopicsClick()
                        "History" -> onHistoryClick()
                        "Profile" -> onProfileClick()
                    }
                },
                icon     = { Icon(icon, contentDescription = label) },
                label    = { Text(label) },
                colors   = NavigationBarItemDefaults.colors(
                    selectedIconColor   = selectedColor,
                    selectedTextColor   = selectedColor,
                    unselectedIconColor = unselectedColor,
                    unselectedTextColor = unselectedColor,
                    indicatorColor      = Color.Transparent // no pill background
                )
            )
        }
    }
}

/*
 * Previews
 *
 * Four previews cover all combinations of orientation × theme so the design
 * can be verified in Android Studio without running on a device.
 */

/** Portrait light preview. */
@Preview(showBackground = true, backgroundColor = 0xFFF3F4F6, uiMode = Configuration.UI_MODE_NIGHT_NO,  widthDp = 390, heightDp = 844)
@Composable private fun HomeScreenLightPreview() { StudentApplicationTheme { Surface { HomeScreen() } } }

/** Portrait dark preview. */
@Preview(showBackground = true, backgroundColor = 0xFF000B1B, uiMode = Configuration.UI_MODE_NIGHT_YES, widthDp = 390, heightDp = 844)
@Composable private fun HomeScreenDarkPreview()  { StudentApplicationTheme { Surface { HomeScreen() } } }

/** Landscape light preview. */
@Preview(showBackground = true, backgroundColor = 0xFFF3F4F6, uiMode = Configuration.UI_MODE_NIGHT_NO,  widthDp = 844, heightDp = 390)
@Composable private fun HomeScreenLandscapeLightPreview() { StudentApplicationTheme { Surface { HomeScreen() } } }

/** Landscape dark preview. */
@Preview(showBackground = true, backgroundColor = 0xFF000B1B, uiMode = Configuration.UI_MODE_NIGHT_YES, widthDp = 844, heightDp = 390)
@Composable private fun HomeScreenLandscapeDarkPreview()  { StudentApplicationTheme { Surface { HomeScreen() } } }