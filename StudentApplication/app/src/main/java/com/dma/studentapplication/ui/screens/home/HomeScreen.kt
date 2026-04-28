package com.dma.studentapplication.ui.screens.home

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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dma.studentapplication.ui.components.RoboBuddy
import com.dma.studentapplication.ui.screens.welcome.NickName
import com.dma.studentapplication.ui.theme.StudentApplicationTheme
import com.dma.studentapplication.utils.constants.greeting

// ── Orientation helper ────────────────────────────────────────────────────────

/** Returns true when the device is currently in landscape orientation. */
private val isLandscape: Boolean
    @Composable get() = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

// ── Main screen ───────────────────────────────────────────────────────────────

/**
 * Home screen — the app's main landing page after the splash screen.
 *
 * Shows:
 * - Personalised greeting header with RoboBuddy avatar
 * - Last quiz card (real data or empty state)
 * - Horizontally scrollable quick-topic chips
 * - Vertical "Continue Studying" topic list
 * - Summary stats card
 * - Bottom nav bar (portrait) or side nav rail (landscape)
 *
 * @param userName        Display name shown in the greeting.
 * @param lastQuizTopic   Topic name of the most recently completed quiz, or null if none.
 * @param lastQuizScore   Number of correct answers in the last quiz, or null if none.
 * @param lastQuizTotal   Total questions in the last quiz, or null if none.
 * @param onDailyQuizClick  Called when the last quiz card is tapped.
 * @param onTopicClick    Called with the topic id when a topic card is tapped.
 * @param onTopicsClick   Called when the Topics nav item is tapped.
 * @param onHistoryClick  Called when the History nav item is tapped.
 * @param onProfileClick  Called when the Profile nav item or avatar is tapped.
 */
@Composable
fun HomeScreen(
    userName: String = "Astrea",
    lastQuizTopic: String? = null,
    lastQuizScore: Int? = null,
    lastQuizTotal: Int? = null,
    onDailyQuizClick: () -> Unit = {},
    onTopicClick: (String) -> Unit = {},
    onTopicsClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val isDark    = isSystemInDarkTheme()
    val landscape = isLandscape

    // ── Theme-aware colors ────────────────────────────────────────────────────
    val screenBg       = if (isDark) Color(0xFF000B1B) else Color(0xFFF3F4F6)
    val surfaceColor   = if (isDark) Color(0xFF071833) else Color.White
    val cardBorder     = if (isDark) Color.White.copy(alpha = 0.08f) else Color(0xFFE6ECF2)
    val titleColor     = if (isDark) Color.White else Color(0xFF0B1B4A)
    val subtitleColor  = if (isDark) Color(0xFFB8C4E0) else Color(0xFF5B6785)
    val bottomBarColor = if (isDark) Color(0xFF041225) else Color.White
    val selectedNav    = Color(0xFF27D17F)
    val unselectedNav  = if (isDark) Color(0xFF8FA3C8) else Color(0xFF6B7280)

    // Gradient applied to both the real last quiz card and the empty state card
    val lastQuizGradient = if (isDark)
        Brush.horizontalGradient(listOf(Color(0xFF14838A), Color(0xFF25D39F)))
    else
        Brush.horizontalGradient(listOf(Color(0xFF22C55E), Color(0xFF4ADE80)))

    // Topic list is stable — wrapped in remember so it isn't recreated on recomposition
    val topics = remember {
        listOf(
            HomeTopicUi("math",            "Math",            Icons.Default.Calculate,        Color(0xFF22C55E), Color(0xFFEAF8EE), Color(0xFF0C2540), 10),
            HomeTopicUi("history",         "History",         Icons.Outlined.AutoStories,     Color(0xFF4B83FF), Color(0xFFEEF4FF), Color(0xFF10284A), 10),
            HomeTopicUi("science",         "Science",         Icons.Default.Science,          Color(0xFF8B5CF6), Color(0xFFF3EDFF), Color(0xFF1A234B), 10),
            HomeTopicUi("programming",     "Programming",     Icons.Default.Code,             Color(0xFF10CBE8), Color(0xFFEAFBFF), Color(0xFF102848), 10),
            HomeTopicUi("movies",          "Movies",          Icons.Default.LocalMovies,      Color(0xFFF45B87), Color(0xFFFFEEF3), Color(0xFF2A1C3F), 10),
            HomeTopicUi("sports",          "Sports",          Icons.Default.SportsBasketball, Color(0xFFF59E0B), Color(0xFFFFF5E6), Color(0xFF3A2810), 10),
            HomeTopicUi("geography",       "Geography",       Icons.Default.Public,           Color(0xFF14B87A), Color(0xFFEAFBF5), Color(0xFF102A2B), 10),
            HomeTopicUi("technology",      "Technology",      Icons.Default.Devices,          Color(0xFF6366F1), Color(0xFFE0E7FF), Color(0xFF12123A), 10),
            HomeTopicUi("networking",      "Networking",      Icons.Default.Router,           Color(0xFF0EA5E9), Color(0xFFE0F2FE), Color(0xFF0C1A2E), 10),
            HomeTopicUi("current_affairs", "Current Affairs", Icons.Default.Newspaper,        Color(0xFFEF4444), Color(0xFFFEE2E2), Color(0xFF2A0E0E), 10)
        )
    }

    if (landscape) {
        // Landscape: side nav rail on the left, scrollable content fills the rest
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(screenBg)
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
                modifier         = Modifier.weight(1f),
                userName         = userName,
                isDark           = isDark,
                landscape        = true,
                lastQuizTopic    = lastQuizTopic,
                lastQuizScore    = lastQuizScore,
                lastQuizTotal    = lastQuizTotal,
                surfaceColor     = surfaceColor,
                cardBorder       = cardBorder,
                titleColor       = titleColor,
                subtitleColor    = subtitleColor,
                lastQuizGradient = lastQuizGradient,
                topics           = topics,
                onDailyQuizClick = onDailyQuizClick,
                onTopicClick     = onTopicClick,
                onProfileClick   = onProfileClick
            )
        }
    } else {
        // Portrait: content fills the screen with bottom nav bar overlaid at the bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(screenBg)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            ContentColumn(
                modifier         = Modifier.fillMaxSize(),
                userName         = userName,
                isDark           = isDark,
                landscape        = false,
                lastQuizTopic    = lastQuizTopic,
                lastQuizScore    = lastQuizScore,
                lastQuizTotal    = lastQuizTotal,
                surfaceColor     = surfaceColor,
                cardBorder       = cardBorder,
                titleColor       = titleColor,
                subtitleColor    = subtitleColor,
                lastQuizGradient = lastQuizGradient,
                topics           = topics,
                onDailyQuizClick = onDailyQuizClick,
                onTopicClick     = onTopicClick,
                onProfileClick   = onProfileClick
            )

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

// ── Scrollable content ────────────────────────────────────────────────────────

/**
 * The main scrollable body of the home screen.
 *
 * Renders sections in order: header → last quiz → quick topics → continue studying → summary.
 * In landscape mode the topic list is rendered as a 2-column grid instead of a single column.
 */
@Composable
private fun ContentColumn(
    modifier: Modifier,
    userName: String,
    isDark: Boolean,
    landscape: Boolean,
    lastQuizTopic: String? = null,
    lastQuizScore: Int? = null,
    lastQuizTotal: Int? = null,
    surfaceColor: Color,
    cardBorder: Color,
    titleColor: Color,
    subtitleColor: Color,
    lastQuizGradient: Brush,
    topics: List<HomeTopicUi>,
    onDailyQuizClick: () -> Unit,
    onTopicClick: (String) -> Unit,
    onProfileClick: () -> Unit
) {
    LazyColumn(
        modifier            = modifier.padding(horizontal = 25.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding      = PaddingValues(
            top    = 40.dp,
            bottom = if (landscape) 16.dp else 96.dp // Extra bottom padding avoids bottom nav overlap
        )
    ) {
        // Greeting header with user name and RoboBuddy avatar
        item {
            HeaderSection(
                userName      = userName,
                titleColor    = titleColor,
                subtitleColor = subtitleColor,
                isDark        = isDark,
                onProfileClick = onProfileClick
            )
        }

        item { SectionTitle("Last Quiz", Icons.Outlined.CheckCircle, titleColor) }

        // Show real last quiz data if available, otherwise show the empty state card
        item {
            if (lastQuizTopic != null && lastQuizScore != null && lastQuizTotal != null) {
                // Calculate percentage and resolve topic icon from the topics list
                val percentage = ((lastQuizScore.toFloat() / lastQuizTotal.toFloat()) * 100).toInt()
                val icon = topics.find {
                    lastQuizTopic.lowercase().contains(it.title.lowercase())
                }?.icon ?: Icons.Default.Quiz

                LastQuizCard(
                    gradient      = lastQuizGradient,
                    onClick       = onDailyQuizClick,
                    lastTopic     = lastQuizTopic,
                    lastTopicIcon = icon,
                    lastScore     = percentage
                )
            } else {
                // No quiz taken yet — show placeholder with 0% ring
                EmptyLastQuizCard(
                    gradient = lastQuizGradient,
                    onClick  = onDailyQuizClick
                )
            }
        }

        item { SectionTitle("Quick Topics", Icons.Outlined.GridView, titleColor) }

        // Horizontally scrollable topic chips
        item {
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

        item { SectionTitle("Continue Studying", Icons.AutoMirrored.Filled.MenuBook, titleColor) }

        if (landscape) {
            // Landscape: render topics as a 2-column grid to make better use of horizontal space
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
                            // Fill the empty slot when the last row has only one item
                            if (row.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        } else {
            // Portrait: single-column list of topic cards
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

// ── Last quiz cards ───────────────────────────────────────────────────────────

/**
 * Placeholder card shown when no quiz has been completed yet.
 * Matches the visual layout of [LastQuizCard] with a 0% progress ring
 * and a prompt to start the first quiz.
 */
@Composable
private fun EmptyLastQuizCard(
    gradient: Brush,
    onClick: () -> Unit
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text          = "LAST QUIZ",
                        color         = Color.White.copy(alpha = 0.80f),
                        fontSize      = 15.sp,
                        fontWeight    = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Quiz icon in a frosted circle
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Quiz,
                                contentDescription = null,
                                tint     = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text      = "No quiz taken yet",
                            color     = Color.White,
                            fontSize  = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines  = 1,
                            overflow  = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text       = "Tap to start your first quiz!",
                        color      = Color.White.copy(alpha = 0.80f),
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Progress ring — always shows 0% since no quiz has been taken
                Box(
                    contentAlignment = Alignment.Center,
                    modifier         = Modifier.size(62.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = 7.dp.toPx()

                        // Background track (full circle)
                        drawArc(
                            color      = Color.White.copy(alpha = 0.25f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter  = false,
                            style      = Stroke(width = stroke, cap = StrokeCap.Round)
                        )

                        // Foreground arc — sweepAngle 0f means no progress shown
                        drawArc(
                            color      = Color.White,
                            startAngle = -90f,
                            sweepAngle = 0f,
                            useCenter  = false,
                            style      = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                    }

                    Text(
                        text       = "0%",
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
 * Displays the most recently completed quiz with the topic name, icon, and score ring.
 *
 * @param gradient      Horizontal gradient brush applied to the card background.
 * @param onClick       Called when the card is tapped (navigates to that quiz).
 * @param lastTopic     Display name of the last quiz topic.
 * @param lastTopicIcon Icon representing the topic.
 * @param lastScore     Score as a percentage (0–100) used to draw the progress ring.
 */
@Composable
private fun LastQuizCard(
    gradient: Brush,
    onClick: () -> Unit,
    lastTopic: String = "Math Quiz",
    lastTopicIcon: ImageVector = Icons.Default.Calculate,
    lastScore: Int = 65
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text          = "LAST QUIZ",
                        color         = Color.White.copy(alpha = 0.80f),
                        fontSize      = 15.sp,
                        fontWeight    = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Topic icon in a frosted circle
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = lastTopicIcon,
                                contentDescription = null,
                                tint     = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text       = lastTopic,
                            color      = Color.White,
                            fontSize   = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines   = 1,
                            overflow   = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Circular progress ring proportional to the score percentage
                Box(
                    contentAlignment = Alignment.Center,
                    modifier         = Modifier.size(62.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = 7.dp.toPx()

                        // Background track (full circle)
                        drawArc(
                            color      = Color.White.copy(alpha = 0.25f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter  = false,
                            style      = Stroke(width = stroke, cap = StrokeCap.Round)
                        )

                        // Foreground arc — length proportional to score percentage
                        drawArc(
                            color      = Color.White,
                            startAngle = -90f,
                            sweepAngle = 360f * (lastScore / 100f),
                            useCenter  = false,
                            style      = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                    }

                    Text(
                        text       = "$lastScore%",
                        color      = Color.White,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────

/**
 * Greeting row at the top of the screen.
 * Shows "Hi [name]" with a time-based sub-greeting on the left,
 * and a tappable RoboBuddy avatar on the right that navigates to the profile.
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
        Row(
            modifier          = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(start = 14.dp)) {
                Text(
                    text       = "Hi, $userName",
                    color      = titleColor,
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                // Dynamic greeting changes based on time of day
                Text(
                    text       = "${greeting()}!",
                    color      = subtitleColor,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Tappable RoboBuddy avatar — navigates to the profile screen
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

// ── Section title ─────────────────────────────────────────────────────────────

/**
 * Small labeled row used as a section header throughout the screen.
 * Displays a green icon followed by the section title text.
 */
@Composable
private fun SectionTitle(
    title: String,
    icon: ImageVector,
    titleColor: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector        = icon,
            contentDescription = title,
            tint               = Color(0xFF27D17F),
            modifier           = Modifier.size(20.dp)
        )

        Text(
            text       = title,
            color      = titleColor,
            fontSize   = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier   = Modifier.padding(start = 10.dp)
        )
    }
}

// ── Topic cards ───────────────────────────────────────────────────────────────

/**
 * Compact vertical card used in the horizontal quick-topics row.
 * Shows the topic icon above the topic title.
 */
@Composable
private fun QuickTopicCard(
    topic: HomeTopicUi,
    isDark: Boolean,
    onClick: () -> Unit
) {
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
            Icon(
                imageVector        = topic.icon,
                contentDescription = topic.title,
                tint               = topic.iconTint,
                modifier           = Modifier.size(30.dp)
            )

            Text(
                text       = topic.title,
                color      = if (isDark) Color.White else Color(0xFF0F172A),
                fontSize   = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier   = Modifier.padding(top = 10.dp)
            )
        }
    }
}

/**
 * Full-width topic row card used in the "Continue Studying" section.
 * Displays the topic icon, name, question count, and a forward chevron.
 * In landscape mode a [modifier] is passed to share width in a 2-column grid.
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
            // Topic icon with rounded square background
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (isDark) topic.darkBg else topic.lightBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = topic.icon,
                    contentDescription = topic.title,
                    tint               = topic.iconTint,
                    modifier           = Modifier.size(30.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f).padding(start = 14.dp)
            ) {
                Text(
                    text       = topic.title,
                    color      = titleColor,
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )

                Text(
                    text  = "10 questions",
                    color = subtitleColor,
                    fontSize = 14.sp
                )
            }

            // Forward chevron indicating the card is tappable
            Icon(
                imageVector        = Icons.Default.ArrowForwardIos,
                contentDescription = "Open ${topic.title}",
                tint               = subtitleColor,
                modifier           = Modifier.size(18.dp)
            )
        }
    }
}

// ── Summary card ──────────────────────────────────────────────────────────────

/**
 * Three-column stats card at the bottom of the screen.
 * Shows quizzes completed, average accuracy, and questions per quiz.
 * Values are currently static — wire up to real data when available.
 */
@Composable
private fun SummaryCard(
    surfaceColor: Color,
    borderColor: Color,
    titleColor: Color,
    subtitleColor: Color
) {
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
 * Single stat column inside [SummaryCard].
 *
 * @param icon  Icon shown above the value.
 * @param value The primary numeric or text value.
 * @param label Two-line descriptive label below the value.
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
    Column(
        modifier            = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = null,
            tint               = Color(0xFF27D17F),
            modifier           = Modifier.size(22.dp)
        )

        Text(
            text       = value,
            color      = titleColor,
            fontSize   = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier   = Modifier.padding(top = 4.dp)
        )

        Text(
            text       = label,
            color      = subtitleColor,
            fontSize   = 12.sp,
            lineHeight = 16.sp
        )
    }
}

// ── Navigation ────────────────────────────────────────────────────────────────

/**
 * Landscape side navigation rail with Home (selected), Topics, History, and Profile items.
 * Home is always highlighted since this is the home screen.
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
    val navItems = remember {
        listOf(
            Icons.Default.Home     to "Home",
            Icons.Outlined.GridView to "Topics",
            Icons.Default.History  to "History",
            Icons.Default.Person   to "Profile"
        )
    }

    NavigationRail(
        modifier       = Modifier.fillMaxHeight(),
        containerColor = containerColor
    ) {
        Spacer(Modifier.height(8.dp))

        // index == 0 keeps Home permanently selected on this screen
        navItems.forEachIndexed { index, item ->
            val icon  = item.first
            val label = item.second

            NavigationRailItem(
                selected = index == 0,
                onClick  = {
                    when (label) {
                        "Topics"  -> onTopicsClick()
                        "History" -> onHistoryClick()
                        "Profile" -> onProfileClick()
                    }
                },
                icon   = { Icon(icon, contentDescription = label) },
                label  = { Text(label, fontSize = 12.sp) },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor   = selectedColor,
                    selectedTextColor   = selectedColor,
                    unselectedIconColor = unselectedColor,
                    unselectedTextColor = unselectedColor,
                    indicatorColor      = Color.Transparent
                )
            )
        }
    }
}

/**
 * Portrait bottom navigation bar with Home (selected), Topics, History, and Profile items.
 * Overlaid at the bottom of the screen using a [Box] alignment in the parent.
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
    val navItems = remember {
        listOf(
            Icons.Default.Home      to "Home",
            Icons.Outlined.GridView to "Topics",
            Icons.Default.History   to "History",
            Icons.Default.Person    to "Profile"
        )
    }

    NavigationBar(
        modifier       = modifier.fillMaxWidth(),
        containerColor = containerColor,
        tonalElevation = 0.dp
    ) {
        // index == 0 keeps Home permanently selected on this screen
        navItems.forEachIndexed { index, item ->
            val icon  = item.first
            val label = item.second

            NavigationBarItem(
                selected = index == 0,
                onClick  = {
                    when (label) {
                        "Topics"  -> onTopicsClick()
                        "History" -> onHistoryClick()
                        "Profile" -> onProfileClick()
                    }
                },
                icon   = { Icon(icon, contentDescription = label) },
                label  = { Text(label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = selectedColor,
                    selectedTextColor   = selectedColor,
                    unselectedIconColor = unselectedColor,
                    unselectedTextColor = unselectedColor,
                    indicatorColor      = Color.Transparent
                )
            )
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFF3F4F6, uiMode = Configuration.UI_MODE_NIGHT_NO,  widthDp = 390, heightDp = 844)
@Composable
private fun HomeScreenLightPreview() {
    StudentApplicationTheme { Surface { HomeScreen() } }
}

@Preview(showBackground = true, backgroundColor = 0xFF000B1B, uiMode = Configuration.UI_MODE_NIGHT_YES, widthDp = 390, heightDp = 844)
@Composable
private fun HomeScreenDarkPreview() {
    StudentApplicationTheme { Surface { HomeScreen() } }
}

@Preview(showBackground = true, backgroundColor = 0xFFF3F4F6, uiMode = Configuration.UI_MODE_NIGHT_NO,  widthDp = 844, heightDp = 390)
@Composable
private fun HomeScreenLandscapeLightPreview() {
    StudentApplicationTheme { Surface { HomeScreen() } }
}

@Preview(showBackground = true, backgroundColor = 0xFF000B1B, uiMode = Configuration.UI_MODE_NIGHT_YES, widthDp = 844, heightDp = 390)
@Composable
private fun HomeScreenLandscapeDarkPreview() {
    StudentApplicationTheme { Surface { HomeScreen() } }
}