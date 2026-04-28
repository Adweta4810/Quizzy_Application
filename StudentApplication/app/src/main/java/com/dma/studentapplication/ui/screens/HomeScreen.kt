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

private val isLandscape: Boolean
    @Composable get() = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

@Composable
fun HomeScreen(
    userName: String = "Astrea",
    onDailyQuizClick: () -> Unit = {},
    onTopicClick: (String) -> Unit = {},
    onTopicsClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()
    val landscape = isLandscape

    val screenBg = if (isDark) Color(0xFF000B1B) else Color(0xFFF3F4F6)
    val surfaceColor = if (isDark) Color(0xFF071833) else Color.White
    val cardBorder = if (isDark) Color.White.copy(alpha = 0.08f) else Color(0xFFE6ECF2)
    val titleColor = if (isDark) Color.White else Color(0xFF0B1B4A)
    val subtitleColor = if (isDark) Color(0xFFB8C4E0) else Color(0xFF5B6785)
    val bottomBarColor = if (isDark) Color(0xFF041225) else Color.White
    val selectedNav = Color(0xFF27D17F)
    val unselectedNav = if (isDark) Color(0xFF8FA3C8) else Color(0xFF6B7280)

    val lastQuizGradient = if (isDark)
        Brush.horizontalGradient(listOf(Color(0xFF14838A), Color(0xFF25D39F)))
    else
        Brush.horizontalGradient(listOf(Color(0xFF22C55E), Color(0xFF4ADE80)))

    val topics = remember {
        listOf(
            HomeTopicUi("math", "Math", Icons.Default.Calculate, Color(0xFF22C55E), Color(0xFFEAF8EE), Color(0xFF0C2540), 10),
            HomeTopicUi("history", "History", Icons.Outlined.AutoStories, Color(0xFF4B83FF), Color(0xFFEEF4FF), Color(0xFF10284A), 10),
            HomeTopicUi("science", "Science", Icons.Default.Science, Color(0xFF8B5CF6), Color(0xFFF3EDFF), Color(0xFF1A234B), 10),
            HomeTopicUi("programming", "Programming", Icons.Default.Code, Color(0xFF10CBE8), Color(0xFFEAFBFF), Color(0xFF102848), 10),
            HomeTopicUi("movies", "Movies", Icons.Default.LocalMovies, Color(0xFFF45B87), Color(0xFFFFEEF3), Color(0xFF2A1C3F), 10),
            HomeTopicUi("sports", "Sports", Icons.Default.SportsBasketball, Color(0xFFF59E0B), Color(0xFFFFF5E6), Color(0xFF3A2810), 10),
            HomeTopicUi("geography", "Geography", Icons.Default.Public, Color(0xFF14B87A), Color(0xFFEAFBF5), Color(0xFF102A2B), 10),
            HomeTopicUi("technology", "Technology", Icons.Default.Devices, Color(0xFF6366F1), Color(0xFFE0E7FF), Color(0xFF12123A), 10),
            HomeTopicUi("networking", "Networking", Icons.Default.Router, Color(0xFF0EA5E9), Color(0xFFE0F2FE), Color(0xFF0C1A2E), 10),
            HomeTopicUi("current_affairs", "Current Affairs", Icons.Default.Newspaper, Color(0xFFEF4444), Color(0xFFFEE2E2), Color(0xFF2A0E0E), 10)
        )
    }

    if (landscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(screenBg)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            LandscapeNavRail(
                containerColor = bottomBarColor,
                selectedColor = selectedNav,
                unselectedColor = unselectedNav,
                onTopicsClick = onTopicsClick,
                onHistoryClick = onHistoryClick,
                onProfileClick = onProfileClick
            )

            ContentColumn(
                modifier = Modifier.weight(1f),
                userName = userName,
                isDark = isDark,
                landscape = true,
                surfaceColor = surfaceColor,
                cardBorder = cardBorder,
                titleColor = titleColor,
                subtitleColor = subtitleColor,
                lastQuizGradient = lastQuizGradient,
                topics = topics,
                onDailyQuizClick = onDailyQuizClick,
                onTopicClick = onTopicClick,
                onProfileClick = onProfileClick
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(screenBg)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            ContentColumn(
                modifier = Modifier.fillMaxSize(),
                userName = userName,
                isDark = isDark,
                landscape = false,
                surfaceColor = surfaceColor,
                cardBorder = cardBorder,
                titleColor = titleColor,
                subtitleColor = subtitleColor,
                lastQuizGradient = lastQuizGradient,
                topics = topics,
                onDailyQuizClick = onDailyQuizClick,
                onTopicClick = onTopicClick,
                onProfileClick = onProfileClick
            )

            BottomBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                containerColor = bottomBarColor,
                selectedColor = selectedNav,
                unselectedColor = unselectedNav,
                onTopicsClick = onTopicsClick,
                onHistoryClick = onHistoryClick,
                onProfileClick = onProfileClick
            )
        }
    }
}

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
    lastQuizGradient: Brush,
    topics: List<HomeTopicUi>,
    onDailyQuizClick: () -> Unit,
    onTopicClick: (String) -> Unit,
    onProfileClick: () -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 25.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            top = 40.dp,
            bottom = if (landscape) 16.dp else 96.dp
        )
    ) {
        item {
            HeaderSection(
                userName = userName,
                titleColor = titleColor,
                subtitleColor = subtitleColor,
                isDark = isDark,
                onProfileClick = onProfileClick
            )
        }

        item { SectionTitle("Last Quiz", Icons.Outlined.CheckCircle, titleColor) }

        item {
            LastQuizCard(
                gradient = lastQuizGradient,
                onClick = onDailyQuizClick,
                lastTopic = "Math Quiz",
                lastTopicIcon = Icons.Default.Calculate,
                lastScore = 65
            )
        }

        item { SectionTitle("Quick Topics", Icons.Outlined.GridView, titleColor) }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(topics) { topic ->
                    QuickTopicCard(
                        topic = topic,
                        isDark = isDark,
                        onClick = { onTopicClick(topic.id) }
                    )
                }
            }
        }

        item { SectionTitle("Continue Studying", Icons.AutoMirrored.Filled.MenuBook, titleColor) }

        if (landscape) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    topics.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            row.forEach { topic ->
                                TopicProgressCard(
                                    modifier = Modifier.weight(1f),
                                    topic = topic,
                                    isDark = isDark,
                                    surfaceColor = surfaceColor,
                                    borderColor = cardBorder,
                                    titleColor = titleColor,
                                    subtitleColor = subtitleColor,
                                    onClick = { onTopicClick(topic.id) }
                                )
                            }
                            if (row.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        } else {
            items(topics) { topic ->
                TopicProgressCard(
                    topic = topic,
                    isDark = isDark,
                    surfaceColor = surfaceColor,
                    borderColor = cardBorder,
                    titleColor = titleColor,
                    subtitleColor = subtitleColor,
                    onClick = { onTopicClick(topic.id) }
                )
            }
        }

        item {
            SummaryCard(
                surfaceColor = surfaceColor,
                borderColor = cardBorder,
                titleColor = titleColor,
                subtitleColor = subtitleColor
            )
        }
    }
}

@Composable
private fun LastQuizCard(
    gradient: Brush,
    onClick: () -> Unit,
    lastTopic: String = "Math Quiz",
    lastTopicIcon: ImageVector = Icons.Default.Calculate,
    lastScore: Int = 65
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(horizontal = 18.dp, vertical = 18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "LAST QUIZ",
                        color = Color.White.copy(alpha = 0.80f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
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
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = lastTopic,
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(62.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = 7.dp.toPx()

                        drawArc(
                            color = Color.White.copy(alpha = 0.25f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = androidx.compose.ui.graphics.drawscope.Stroke(
                                width = stroke,
                                cap = StrokeCap.Round
                            )
                        )

                        drawArc(
                            color = Color.White,
                            startAngle = -90f,
                            sweepAngle = 360f * (lastScore / 100f),
                            useCenter = false,
                            style = androidx.compose.ui.graphics.drawscope.Stroke(
                                width = stroke,
                                cap = StrokeCap.Round
                            )
                        )
                    }

                    Text(
                        text = "$lastScore%",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(
    userName: String,
    titleColor: Color,
    subtitleColor: Color,
    isDark: Boolean,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(start = 14.dp)) {
                Text(
                    text = "Hi $userName,",
                    color = titleColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = "${greeting()}!",
                    color = subtitleColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

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

@Composable
private fun SectionTitle(
    title: String,
    icon: ImageVector,
    titleColor: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFF27D17F),
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = title,
            color = titleColor,
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}

@Composable
private fun QuickTopicCard(
    topic: HomeTopicUi,
    isDark: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) topic.darkBg else topic.lightBg
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = topic.icon,
                contentDescription = topic.title,
                tint = topic.iconTint,
                modifier = Modifier.size(30.dp)
            )

            Text(
                text = topic.title,
                color = if (isDark) Color.White else Color(0xFF0F172A),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

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
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (isDark) topic.darkBg else topic.lightBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = topic.icon,
                    contentDescription = topic.title,
                    tint = topic.iconTint,
                    modifier = Modifier.size(30.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = topic.title,
                    color = titleColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "10 questions",
                    color = subtitleColor,
                    fontSize = 14.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Open ${topic.title}",
                tint = subtitleColor,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun SummaryCard(
    surfaceColor: Color,
    borderColor: Color,
    titleColor: Color,
    subtitleColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryItem(Icons.Outlined.CheckCircle, "3", "Quizzes\nCompleted", titleColor, subtitleColor, Modifier.weight(1f))
            Divider(modifier = Modifier.size(width = 1.dp, height = 48.dp), color = borderColor)
            SummaryItem(Icons.Default.QueryStats, "80%", "Average\nAccuracy", titleColor, subtitleColor, Modifier.weight(1f))
            Divider(modifier = Modifier.size(width = 1.dp, height = 48.dp), color = borderColor)
            SummaryItem(Icons.Default.School, "10", "Questions\nPer Quiz", titleColor, subtitleColor, Modifier.weight(1f))
        }
    }
}

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
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF27D17F),
            modifier = Modifier.size(22.dp)
        )

        Text(
            text = value,
            color = titleColor,
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = label,
            color = subtitleColor,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}

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
            Icons.Default.Home to "Home",
            Icons.Outlined.GridView to "Topics",
            Icons.Default.History to "History",
            Icons.Default.Person to "Profile"
        )
    }

    NavigationRail(
        modifier = Modifier.fillMaxHeight(),
        containerColor = containerColor
    ) {
        Spacer(Modifier.height(8.dp))

        navItems.forEachIndexed { index, item ->
            val icon = item.first
            val label = item.second

            NavigationRailItem(
                selected = index == 0,
                onClick = {
                    when (label) {
                        "Topics" -> onTopicsClick()
                        "History" -> onHistoryClick()
                        "Profile" -> onProfileClick()
                    }
                },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label, fontSize = 12.sp) },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = selectedColor,
                    selectedTextColor = selectedColor,
                    unselectedIconColor = unselectedColor,
                    unselectedTextColor = unselectedColor,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

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
            Icons.Default.Home to "Home",
            Icons.Outlined.GridView to "Topics",
            Icons.Default.History to "History",
            Icons.Default.Person to "Profile"
        )
    }

    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = containerColor,
        tonalElevation = 0.dp
    ) {
        navItems.forEachIndexed { index, item ->
            val icon = item.first
            val label = item.second

            NavigationBarItem(
                selected = index == 0,
                onClick = {
                    when (label) {
                        "Topics" -> onTopicsClick()
                        "History" -> onHistoryClick()
                        "Profile" -> onProfileClick()
                    }
                },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedColor,
                    selectedTextColor = selectedColor,
                    unselectedIconColor = unselectedColor,
                    unselectedTextColor = unselectedColor,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF3F4F6, uiMode = Configuration.UI_MODE_NIGHT_NO, widthDp = 390, heightDp = 844)
@Composable
private fun HomeScreenLightPreview() {
    StudentApplicationTheme {
        Surface {
            HomeScreen()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000B1B, uiMode = Configuration.UI_MODE_NIGHT_YES, widthDp = 390, heightDp = 844)
@Composable
private fun HomeScreenDarkPreview() {
    StudentApplicationTheme {
        Surface {
            HomeScreen()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF3F4F6, uiMode = Configuration.UI_MODE_NIGHT_NO, widthDp = 844, heightDp = 390)
@Composable
private fun HomeScreenLandscapeLightPreview() {
    StudentApplicationTheme {
        Surface {
            HomeScreen()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000B1B, uiMode = Configuration.UI_MODE_NIGHT_YES, widthDp = 844, heightDp = 390)
@Composable
private fun HomeScreenLandscapeDarkPreview() {
    StudentApplicationTheme {
        Surface {
            HomeScreen()
        }
    }
}