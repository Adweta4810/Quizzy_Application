package com.dma.studentapplication.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.WavingHand
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dma.studentapplication.ui.components.RoboBuddy
import com.dma.studentapplication.ui.theme.StudentApplicationTheme

data class HomeTopicUi(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val iconTint: Color,
    val lightBg: Color,
    val darkBg: Color,
    val questionsLeft: Int
)

@Composable
fun HomeScreen(
    userName: String = "Astrea",
    onDailyQuizClick: () -> Unit = {},
    onTopicClick: (String) -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()

    val screenBg = if (isDark) Color(0xFF000B1B) else Color(0xFFF3F4F6)
    val surfaceColor = if (isDark) Color(0xFF071833) else Color(0xFFFFFFFF)
    val cardBorder = if (isDark) Color.White.copy(alpha = 0.08f) else Color(0xFFE6ECF2)
    val titleColor = if (isDark) Color(0xFFFFFFFF) else Color(0xFF0B1B4A)
    val subtitleColor = if (isDark) Color(0xFFB8C4E0) else Color(0xFF5B6785)
    val bottomBarColor = if (isDark) Color(0xFF041225) else Color(0xFFFFFFFF)
    val selectedNav = Color(0xFF27D17F)
    val unselectedNav = if (isDark) Color(0xFF8FA3C8) else Color(0xFF6B7280)

    val dailyQuizGradient = if (isDark) {
        Brush.horizontalGradient(
            listOf(Color(0xFF14838A), Color(0xFF25D39F))
        )
    } else {
        Brush.horizontalGradient(
            listOf(Color(0xFF22C55E), Color(0xFF4ADE80))
        )
    }

    val topics = remember {
        listOf(
            HomeTopicUi(
                id = "math",
                title = "Math",
                icon = Icons.Default.Calculate,
                iconTint = Color(0xFF22C55E),
                lightBg = Color(0xFFEAF8EE),
                darkBg = Color(0xFF0C2540),
                questionsLeft = 12
            ),
            HomeTopicUi(
                id = "history",
                title = "History",
                icon = Icons.Outlined.AutoStories,
                iconTint = Color(0xFF4B83FF),
                lightBg = Color(0xFFEEF4FF),
                darkBg = Color(0xFF10284A),
                questionsLeft = 6
            ),
            HomeTopicUi(
                id = "science",
                title = "Science",
                icon = Icons.Default.Science,
                iconTint = Color(0xFF8B5CF6),
                lightBg = Color(0xFFF3EDFF),
                darkBg = Color(0xFF1A234B),
                questionsLeft = 20
            ),
            HomeTopicUi(
                id = "programming",
                title = "Programming",
                icon = Icons.Default.Code,
                iconTint = Color(0xFF10CBE8),
                lightBg = Color(0xFFEAFBFF),
                darkBg = Color(0xFF102848),
                questionsLeft = 8
            ),
            HomeTopicUi(
                id = "movies",
                title = "Movies",
                icon = Icons.Default.LocalMovies,
                iconTint = Color(0xFFF45B87),
                lightBg = Color(0xFFFFEEF3),
                darkBg = Color(0xFF2A1C3F),
                questionsLeft = 5
            ),
            HomeTopicUi(
                id = "sports",
                title = "Sports",
                icon = Icons.Default.SportsBasketball,
                iconTint = Color(0xFFF59E0B),
                lightBg = Color(0xFFFFF5E6),
                darkBg = Color(0xFF3A2810),
                questionsLeft = 9
            ),
            HomeTopicUi(
                id = "geography",
                title = "Geography",
                icon = Icons.Default.Public,
                iconTint = Color(0xFF14B87A),
                lightBg = Color(0xFFEAFBF5),
                darkBg = Color(0xFF102A2B),
                questionsLeft = 7
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBg)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.padding(top = 16.dp)) }

            item {
                HeaderSection(
                    userName = userName,
                    titleColor = titleColor,
                    subtitleColor = subtitleColor,
                    isDark = isDark,
                    onProfileClick = onProfileClick
                )
            }

            item {
                SectionTitle(
                    title = "Practice More",
                    icon = Icons.Outlined.CheckCircle,
                    titleColor = titleColor
                )
            }

            item {
                DailyQuizCard(
                    gradient = dailyQuizGradient,
                    onClick = onDailyQuizClick
                )
            }

            item {
                SectionTitle(
                    title = "Quick Topics",
                    icon = Icons.Outlined.GridView,
                    titleColor = titleColor
                )
            }

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

            item {
                SectionTitle(
                    title = "Continue Studying",
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    titleColor = titleColor
                )
            }

            items(topics.take(3)) { topic ->
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

            item {
                SummaryCard(
                    surfaceColor = surfaceColor,
                    borderColor = cardBorder,
                    titleColor = titleColor,
                    subtitleColor = subtitleColor
                )
            }

            item {
                Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
                Spacer(modifier = Modifier.padding(bottom = 72.dp))
            }
        }

        BottomBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            containerColor = bottomBarColor,
            selectedColor = selectedNav,
            unselectedColor = unselectedNav
        )
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
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(if (isDark) Color(0xFF052337) else Color(0x1427D17F)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.WavingHand,
                    contentDescription = "Greeting",
                    tint = Color(0xFF27D17F),
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(modifier = Modifier.padding(start = 14.dp)) {
                Text(
                    text = "Hi $userName,",
                    color = titleColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Great to see you again!",
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
private fun DailyQuizCard(
    gradient: Brush,
    onClick: () -> Unit
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
                .padding(horizontal = 18.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "3h",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                ) {
                    Text(
                        text = "Daily Quiz",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "20 mixed questions",
                        color = Color.White.copy(alpha = 0.95f),
                        fontSize = 14.sp,
                        maxLines = 1
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.95f),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.padding(top = 8.dp))

                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = "Open Daily Quiz",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
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
                fontSize = 13.sp,
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
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
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
                    text = "${topic.questionsLeft} questions left",
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
            SummaryItem(
                icon = Icons.Outlined.CheckCircle,
                value = "3",
                label = "Quizzes\nCompleted",
                titleColor = titleColor,
                subtitleColor = subtitleColor,
                modifier = Modifier.weight(1f)
            )

            Divider(
                modifier = Modifier.size(width = 1.dp, height = 48.dp),
                color = borderColor
            )

            SummaryItem(
                icon = Icons.Default.QueryStats,
                value = "80%",
                label = "Average\nAccuracy",
                titleColor = titleColor,
                subtitleColor = subtitleColor,
                modifier = Modifier.weight(1f)
            )

            Divider(
                modifier = Modifier.size(width = 1.dp, height = 48.dp),
                color = borderColor
            )

            SummaryItem(
                icon = Icons.Default.School,
                value = "10",
                label = "Questions\nPer Quiz",
                titleColor = titleColor,
                subtitleColor = subtitleColor,
                modifier = Modifier.weight(1f)
            )
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
private fun BottomBar(
    modifier: Modifier = Modifier,
    containerColor: Color,
    selectedColor: Color,
    unselectedColor: Color
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = containerColor,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Outlined.GridView, contentDescription = "Topics") },
            label = { Text("Topics") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.History, contentDescription = "History") },
            label = { Text("History") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
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

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF3F4F6,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun HomeScreenLightPreview() {
    StudentApplicationTheme {
        Surface {
            HomeScreen()
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000B1B,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun HomeScreenDarkPreview() {
    StudentApplicationTheme {
        Surface {
            HomeScreen()
        }
    }
}