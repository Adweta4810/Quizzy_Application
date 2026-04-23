package com.dma.studentapplication.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dma.studentapplication.model.RoboBuddyState
import com.dma.studentapplication.ui.components.RoboBuddy

@Composable
fun ResultScreen(
    score: Int,
    totalQuestions: Int,
    timeTakenText: String = "2:37",
    topicTitle: String = "Technology Quiz",
    onReviewLessonClick: () -> Unit = {},
    onRestartQuizClick: () -> Unit = {},
    onBackHomeClick: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()

    val percentage = if (totalQuestions > 0) {
        ((score.toFloat() / totalQuestions.toFloat()) * 100).toInt()
    } else {
        0
    }

    val mistakes = (totalQuestions - score).coerceAtLeast(0)

    val roboState = when {
        percentage >= 80 -> RoboBuddyState.CORRECT
        percentage >= 50 -> RoboBuddyState.THINKING
        else -> RoboBuddyState.SAD
    }

    val title = when {
        percentage >= 80 -> "Amazing!"
        percentage >= 50 -> "Good effort"
        else -> "Keep trying"
    }

    val subtitle = when {
        percentage >= 80 -> "Robo is proud of your progress!"
        percentage >= 50 -> "Nice work. A little more practice will help a lot."
        else -> "Review the lesson and try again."
    }

    val ringColor = when {
        percentage >= 80 -> Color(0xFF22C55E)
        percentage >= 50 -> Color(0xFFF59E0B)
        else -> Color(0xFFEF4444)
    }

    val screenBgTop = if (isDark) Color(0xFF000B1B) else Color(0xFFF3F7FC)
    val screenBgBottom = if (isDark) Color(0xFF05162D) else Color(0xFFEAF2FB)

    val titleColor = if (isDark) Color.White else Color(0xFF111827)
    val subtitleColor = if (isDark) Color(0xFFB7C4D8) else Color(0xFF6B7280)
    val borderColor = if (isDark) Color.White.copy(alpha = 0.08f) else Color(0xFFE5E7EB)

    val primaryColor = Color(0xFF27D17F)
    val accentTeal = Color(0xFF16C6D9)

    val mistakesCardBg = if (isDark) Color(0xFF2C1520) else Color(0xFFFFF1F2)
    val correctCardBg = if (isDark) Color(0xFF122A24) else Color(0xFFECFDF5)

    val mistakesTextColor = if (isDark) Color(0xFFFF8E96) else Color(0xFFDC2626)
    val correctTextColor = if (isDark) Color(0xFF6EE7B7) else Color(0xFF16A34A)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(screenBgTop, screenBgBottom)
                )
            )
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScoreRing(
                percentage = percentage,
                ringColor = ringColor,
                isDark = isDark,
                roboState = roboState
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = title,
                color = titleColor,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitle,
                color = subtitleColor,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.92f)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Time Taken",
                    tint = Color(0xFFEAB308),
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = timeTakenText,
                    color = Color(0xFFEAB308),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = topicTitle,
                color = accentTeal,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    number = mistakes.toString(),
                    label = "Mistakes",
                    textColor = mistakesTextColor,
                    backgroundColor = mistakesCardBg,
                    iconText = "✕"
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    number = score.toString(),
                    label = "Correct",
                    textColor = correctTextColor,
                    backgroundColor = correctCardBg,
                    iconText = "✓"
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onReviewLessonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = if (isDark) Color(0xFF052012) else Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "Review Lesson",
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Review Lesson",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            OutlinedButton(
                onClick = onRestartQuizClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, borderColor),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = titleColor
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Restart Quiz",
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Restart Quiz",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            OutlinedButton(
                onClick = onBackHomeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, borderColor),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = titleColor
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Back Home",
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Back Home",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ScoreRing(
    percentage: Int,
    ringColor: Color,
    isDark: Boolean,
    roboState: RoboBuddyState
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(190.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 14.dp.toPx()

            drawArc(
                color = if (isDark) Color.White.copy(alpha = 0.14f) else Color(0xFFD1D5DB),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )

            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = 360f * (percentage / 100f),
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )
        }

        Surface(
            modifier = Modifier.size(126.dp),
            shape = RoundedCornerShape(30.dp),
            color = if (isDark) Color(0xFF0B1B33) else Color(0xFFF8FAFC),
            shadowElevation = if (isDark) 0.dp else 4.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                RoboBuddy(
                    state = roboState,
                    modifier = Modifier.size(76.dp)
                )
            }
        }

        Text(
            text = "$percentage%",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp),
            color = ringColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    number: String,
    label: String,
    textColor: Color,
    backgroundColor: Color,
    iconText: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = number,
                color = textColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "$iconText  $label",
                color = textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(
    name = "Result Light",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true
)
@Composable
private fun ResultScreenLightPreview() {
    ResultScreen(
        score = 8,
        totalQuestions = 10,
        timeTakenText = "2:37",
        topicTitle = "Technology Quiz"
    )
}

@Preview(
    name = "Result Dark",
    showBackground = true,
    backgroundColor = 0xFF000B1B,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true
)
@Composable
private fun ResultScreenDarkPreview() {
    ResultScreen(
        score = 3,
        totalQuestions = 10,
        timeTakenText = "2:37",
        topicTitle = "Technology Quiz"
    )
}