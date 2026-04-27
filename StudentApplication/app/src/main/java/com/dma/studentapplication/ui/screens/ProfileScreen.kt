package com.dma.studentapplication.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.dma.studentapplication.ui.theme.StudentApplicationTheme

@Composable
fun ProfileScreen(
    userName: String = "Astrea",
    email: String = "astrea@student.com",
    quizzesCompleted: Int = 12,
    bestScore: String = "9/10",
    onBack: () -> Unit = {},
    onHomeClick: () -> Unit = onBack,
    onTopicsClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onLeaderboardClick: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val backgroundColor = if (isDark) Color(0xFF000B1B) else Color(0xFFF3F4F6)
    val cardColor = if (isDark) Color(0xFF071833) else Color.White
    val textDark = if (isDark) Color.White else Color(0xFF0B1B4A)
    val textMuted = if (isDark) Color(0xFFB8C4E0) else Color(0xFF5B6785)
    val primaryGreen = Color(0xFF27D17F)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                ProfileSideNavBar(
                    current = "profile",
                    isDark = isDark,
                    onHomeClick = onHomeClick,
                    onTopicsClick = onTopicsClick,
                    onHistoryClick = onHistoryClick,
                    onProfileClick = {}
                )

                ProfileContent(
                    modifier = Modifier.weight(1f),
                    userName = userName,
                    email = email,
                    quizzesCompleted = quizzesCompleted,
                    bestScore = bestScore,
                    cardColor = cardColor,
                    textDark = textDark,
                    textMuted = textMuted,
                    primaryGreen = primaryGreen,
                    onBack = onBack,
                    onHomeClick = onHomeClick,
                    onHistoryClick = onHistoryClick,
                    onLeaderboardClick = onLeaderboardClick
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                ProfileContent(
                    modifier = Modifier.weight(1f),
                    userName = userName,
                    email = email,
                    quizzesCompleted = quizzesCompleted,
                    bestScore = bestScore,
                    cardColor = cardColor,
                    textDark = textDark,
                    textMuted = textMuted,
                    primaryGreen = primaryGreen,
                    onBack = onBack,
                    onHomeClick = onHomeClick,
                    onHistoryClick = onHistoryClick,
                    onLeaderboardClick = onLeaderboardClick
                )

                AppBottomNavBar(
                    current = "profile",
                    onHomeClick = onHomeClick,
                    onTopicsClick = onTopicsClick,
                    onHistoryClick = onHistoryClick,
                    onProfileClick = {},
                    isDark = isDark
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    modifier: Modifier = Modifier,
    userName: String,
    email: String,
    quizzesCompleted: Int,
    bestScore: String,
    cardColor: Color,
    textDark: Color,
    textMuted: Color,
    primaryGreen: Color,
    onBack: () -> Unit,
    onHomeClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onLeaderboardClick: () -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            start = 22.dp,
            end = 22.dp,
            top = 24.dp,
            bottom = 24.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = textDark
                    )
                }

                Text(
                    text = "Profile",
                    color = textDark,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        item {
            Box(
                modifier = Modifier
                    .size(108.dp)
                    .clip(CircleShape)
                    .background(primaryGreen.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = primaryGreen,
                    modifier = Modifier.size(62.dp)
                )
            }
        }

        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = userName,
                    color = textDark,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = email,
                    color = textMuted,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileStatCard(
                    title = "Quizzes",
                    value = quizzesCompleted.toString(),
                    icon = Icons.Default.Quiz,
                    cardColor = cardColor,
                    textDark = textDark,
                    textMuted = textMuted,
                    primaryGreen = primaryGreen,
                    modifier = Modifier.weight(1f)
                )

                ProfileStatCard(
                    title = "Best Score",
                    value = bestScore,
                    icon = Icons.Default.Star,
                    cardColor = cardColor,
                    textDark = textDark,
                    textMuted = textMuted,
                    primaryGreen = primaryGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Button(
                onClick = onHistoryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryGreen,
                    contentColor = Color(0xFF000B1B)
                )
            ) {
                Text("View Quiz History", fontWeight = FontWeight.Bold)
            }
        }

        item {
            Button(
                onClick = onLeaderboardClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryGreen,
                    contentColor = Color(0xFF000B1B)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("View Leaderboard", fontWeight = FontWeight.Bold)
            }
        }

        item {
            OutlinedButton(
                onClick = onHomeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = textDark
                )
            ) {
                Text("Back to Home", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ProfileStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    cardColor: Color,
    textDark: Color,
    textMuted: Color,
    primaryGreen: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(126.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = primaryGreen,
                modifier = Modifier.size(30.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = value,
                color = textDark,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = title,
                color = textMuted,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ProfileSideNavBar(
    current: String,
    isDark: Boolean,
    onHomeClick: () -> Unit,
    onTopicsClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val navBg = if (isDark) Color(0xFF071833) else Color.White
    val selected = Color(0xFF27D17F)
    val unselected = if (isDark) Color(0xFFB8C4E0) else Color(0xFF6B7280)

    Surface(
        modifier = Modifier
            .width(110.dp)
            .fillMaxHeight()
            .padding(start = 14.dp, top = 12.dp, bottom = 12.dp),
        color = navBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileSideNavItem(
                title = "Home",
                icon = Icons.Default.Home,
                selected = current == "home",
                selectedColor = selected,
                unselectedColor = unselected,
                onClick = onHomeClick
            )

            ProfileSideNavItem(
                title = "Topics",
                icon = Icons.Default.GridView,
                selected = current == "topics",
                selectedColor = selected,
                unselectedColor = unselected,
                onClick = onTopicsClick
            )

            ProfileSideNavItem(
                title = "History",
                icon = Icons.Default.History,
                selected = current == "history",
                selectedColor = selected,
                unselectedColor = unselected,
                onClick = onHistoryClick
            )

            ProfileSideNavItem(
                title = "Profile",
                icon = Icons.Default.Person,
                selected = current == "profile",
                selectedColor = selected,
                unselectedColor = unselected,
                onClick = onProfileClick
            )
        }
    }
}

@Composable
private fun ProfileSideNavItem(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    selectedColor: Color,
    unselectedColor: Color,
    onClick: () -> Unit
) {
    val color = if (selected) selectedColor else unselectedColor

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = color,
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            color = color,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    StudentApplicationTheme {
        ProfileScreen()
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    widthDp = 900,
    heightDp = 420
)
@Composable
fun ProfileScreenLandscapePreview() {
    StudentApplicationTheme {
        ProfileScreen()
    }
}