package com.dma.studentapplication.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
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
    onHistoryClick: () -> Unit = {},
    onLeaderboardClick: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()

    val backgroundColor = if (isDark) Color(0xFF000B1B) else Color(0xFFF3F4F6)
    val cardColor = if (isDark) Color(0xFF071833) else Color.White
    val textDark = if (isDark) Color.White else Color(0xFF0B1B4A)
    val textMuted = if (isDark) Color(0xFF8FA3C8) else Color(0xFF5B6785)
    val primaryGreen = Color(0xFF27D17F)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

            Spacer(modifier = Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .size(110.dp)
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

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(30.dp))

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

            Spacer(modifier = Modifier.height(24.dp))

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
                Text(
                    text = "View Quiz History",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp).padding(end = 8.dp)
                    )
                    Text(
                        text = "View Leaderboard",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = textDark
                )
            ) {
                Text(text = "Back to Home")
            }
        }
    }
}

@Composable
private fun ProfileStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    cardColor: Color,
    textDark: Color,
    textMuted: Color,
    primaryGreen: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(130.dp),
        shape = RoundedCornerShape(20.dp),
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
                style = MaterialTheme.typography.bodySmall
            )
        }
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
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ProfileScreenDarkPreview() {
    StudentApplicationTheme {
        ProfileScreen()
    }
}