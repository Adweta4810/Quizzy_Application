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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dma.studentapplication.ui.screens.model.QuizHistoryDetail
import com.dma.studentapplication.ui.screens.model.ReviewQuestionItem
import com.dma.studentapplication.ui.theme.StudentApplicationTheme

// ── Theme colors ──────────────────────────────────────────────────────────────

private val PrimaryGreen = Color(0xFF27D17F)  // Primary action / highlight color
private val AccentTeal   = Color(0xFF16C6D9)  // Subtitle accent used in the top bar

// Light mode colors
private val LightBorder   = Color(0xFFE6ECF2)
private val LightTextDark = Color(0xFF0B1B4A)
private val LightTextMuted = Color(0xFF5B6785)

// Dark mode colors
private val DarkBackground = Color(0xFF000B1B)
private val DarkCard       = Color(0xFF071833)
private val DarkBorder     = Color(0xFF1A2A40)
private val DarkTextDark   = Color(0xFFFFFFFF)
private val DarkTextMuted  = Color(0xFF8FA3C8)

// ── Topic metadata ────────────────────────────────────────────────────────────

/**
 * Visual metadata for a quiz topic — icon, tint color, and light/dark backgrounds.
 * Used to render the topic icon in the top bar with the correct theme-aware colors.
 */
private data class DetailTopicMeta(
    val icon: ImageVector,
    val tint: Color,
    val lightBg: Color,
    val darkBg: Color
)

/**
 * Maps topic names to their corresponding [DetailTopicMeta].
 * Add a new entry here whenever a new topic is introduced.
 */
private val detailTopicMeta: Map<String, DetailTopicMeta> = mapOf(
    "Math"        to DetailTopicMeta(Icons.Default.Calculate,       Color(0xFF22C55E), Color(0xFFEAF8EE), Color(0xFF0C2540)),
    "History"     to DetailTopicMeta(Icons.Outlined.AutoStories,    Color(0xFF4B83FF), Color(0xFFEEF4FF), Color(0xFF10284A)),
    "Science"     to DetailTopicMeta(Icons.Default.Science,         Color(0xFF8B5CF6), Color(0xFFF3EDFF), Color(0xFF1A234B)),
    "Programming" to DetailTopicMeta(Icons.Default.Code,            Color(0xFF10CBE8), Color(0xFFEAFBFF), Color(0xFF102848)),
    "Movies"      to DetailTopicMeta(Icons.Default.LocalMovies,     Color(0xFFF45B87), Color(0xFFFFEEF3), Color(0xFF2A1C3F)),
    "Sports"      to DetailTopicMeta(Icons.Default.SportsBasketball,Color(0xFFF59E0B), Color(0xFFFFF5E6), Color(0xFF3A2810)),
    "Geography"   to DetailTopicMeta(Icons.Default.Public,          Color(0xFF14B87A), Color(0xFFEAFBF5), Color(0xFF102A2B))
)

// ── Main screen ───────────────────────────────────────────────────────────────

/**
 * Displays the full detail view for a completed quiz result from history.
 *
 * Shows:
 * - Topic name and icon in the top bar
 * - Score ring with percentage
 * - Date and time taken badges
 * - Correct / mistakes stat cards
 * - Full answer breakdown (per-question review cards)
 * - Retake and Back Home action buttons
 *
 * @param detail           The quiz result data to display.
 * @param isDark           Whether dark mode is active. Defaults to system setting.
 * @param onBackClick      Called when the back arrow is tapped.
 * @param onRetakeQuizClick Called when the user wants to retake the same quiz.
 * @param onBackHomeClick  Called when the user wants to go back to the home screen.
 */
@Composable
fun HistoryDetailScreen(
    detail: QuizHistoryDetail,
    isDark: Boolean = isSystemInDarkTheme(),
    onBackClick: () -> Unit = {},
    onRetakeQuizClick: () -> Unit = {},
    onBackHomeClick: () -> Unit = {}
) {
    // ── Theme-aware colors ────────────────────────────────────────────────────
    val screenBgTop    = if (isDark) DarkBackground else Color(0xFFF3F7FC)
    val screenBgBottom = if (isDark) Color(0xFF05162D) else Color(0xFFEAF2FB)
    val borderColor    = if (isDark) DarkBorder else LightBorder
    val textDark       = if (isDark) DarkTextDark else LightTextDark
    val textMuted      = if (isDark) DarkTextMuted else LightTextMuted

    // Answer card colors
    val correctCardBg    = if (isDark) Color(0xFF10271F) else Color(0xFFECFDF5)
    val wrongCardBg      = if (isDark) Color(0xFF2B1520) else Color(0xFFFFF1F2)
    val correctBorder    = if (isDark) Color(0xFF1F8F63) else Color(0xFF86EFAC)
    val wrongBorder      = if (isDark) Color(0xFFB94A64) else Color(0xFFFDA4AF)
    val correctTextColor = if (isDark) Color(0xFF6EE7B7) else Color(0xFF15803D)
    val wrongTextColor   = if (isDark) Color(0xFFFF9AA5) else Color(0xFFDC2626)

    // ── Score calculation ─────────────────────────────────────────────────────
    // Parse the "correct/total" score string from the history item
    val scoreParts = detail.historyItem.score.split("/")
    val correct    = scoreParts.getOrNull(0)?.trim()?.toIntOrNull() ?: 0
    val total      = scoreParts.getOrNull(1)?.trim()?.toIntOrNull()
        ?.takeIf { it > 0 } ?: detail.totalQuestions.coerceAtLeast(1)

    val percentage = ((correct.toFloat() / total.toFloat()) * 100).toInt()
    val mistakes   = (total - correct).coerceAtLeast(0)

    // Ring color changes based on performance threshold
    val ringColor = when {
        percentage >= 80 -> Color(0xFF22C55E) // Green  — great score
        percentage >= 50 -> Color(0xFFF59E0B) // Amber  — average score
        else             -> Color(0xFFEF4444) // Red    — poor score
    }

    // Look up topic icon/color metadata; null if topic is not in the map
    val meta = detailTopicMeta[detail.historyItem.topic]

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(screenBgTop, screenBgBottom)))
                .padding(WindowInsets.navigationBars.asPaddingValues()),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 24.dp)
        ) {
            // Top bar — back button + topic name/icon
            item {
                DetailTopBar(
                    topic     = detail.historyItem.topic,
                    isDark    = isDark,
                    meta      = meta,
                    textDark  = textDark,
                    textMuted = textMuted,
                    onBack    = onBackClick
                )
            }

            // Score ring + badges + stat cards + section header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(8.dp))

                    // Circular score ring with percentage in the center
                    DetailScoreRing(
                        percentage = percentage,
                        ringColor  = ringColor,
                        isDark     = isDark
                    )

                    Spacer(Modifier.height(20.dp))

                    // Date and time taken displayed side by side
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DetailBadge(
                            icon  = Icons.Default.EmojiEvents,
                            tint  = PrimaryGreen,
                            text  = detail.historyItem.date,
                            color = textMuted
                        )

                        DetailBadge(
                            icon  = Icons.Default.AccessTime,
                            tint  = Color(0xFFEAB308),
                            text  = detail.timeTaken,
                            color = Color(0xFFEAB308)
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    // Correct and mistakes count cards side by side
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        DetailStatCard(
                            modifier  = Modifier.weight(1f),
                            number    = correct.toString(),
                            label     = "Correct",
                            iconText  = "✓",
                            textColor = correctTextColor,
                            bgColor   = correctCardBg
                        )

                        DetailStatCard(
                            modifier  = Modifier.weight(1f),
                            number    = mistakes.toString(),
                            label     = "Mistakes",
                            iconText  = "✕",
                            textColor = wrongTextColor,
                            bgColor   = wrongCardBg
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    // Section header for the answer breakdown list below
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(20.dp)
                        )

                        Text(
                            text = "Answer Breakdown",
                            color = textDark,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }

                    Spacer(Modifier.height(10.dp))
                }
            }

            // One review card per question answered during the quiz
            itemsIndexed(detail.reviewItems) { index, item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                ) {
                    ReviewQuestionCard(
                        questionNumber   = index + 1,
                        item             = item,
                        isDark           = isDark,
                        titleColor       = textDark,
                        subtitleColor    = textMuted,
                        correctCardBg    = correctCardBg,
                        wrongCardBg      = wrongCardBg,
                        correctBorder    = correctBorder,
                        wrongBorder      = wrongBorder,
                        correctTextColor = correctTextColor,
                        wrongTextColor   = wrongTextColor
                    )
                }
            }

            // Action buttons — retake quiz or go back home
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onRetakeQuizClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryGreen,
                            contentColor   = if (isDark) Color(0xFF052012) else Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Retake Quiz",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = "Retake Quiz", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onBackHomeClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape  = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.dp, borderColor),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = textDark)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Back Home",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = "Back Home", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

// ── Sub-composables ───────────────────────────────────────────────────────────

/**
 * Top bar showing a back arrow on the left and the topic name + subtitle in the center.
 * If [meta] is non-null, a topic icon is displayed alongside the topic name.
 */
@Composable
private fun DetailTopBar(
    topic: String,
    isDark: Boolean,
    meta: DetailTopicMeta?,
    textDark: Color,
    textMuted: Color,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = textDark,
                modifier = Modifier.size(24.dp)
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Only render the topic icon if metadata exists for this topic
            if (meta != null) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isDark) meta.darkBg else meta.lightBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = meta.icon,
                        contentDescription = topic,
                        tint = meta.tint,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(10.dp))
            }

            Column {
                Text(
                    text = topic,
                    color = textDark,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Quiz Details",
                    color = AccentTeal,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Spacer balances the back button so the title stays visually centered
        Spacer(Modifier.size(48.dp))
    }
}

/**
 * Circular arc ring displaying the score percentage.
 * The ring color reflects performance (green / amber / red).
 * The percentage and "Score" label are shown in the center.
 */
@Composable
private fun DetailScoreRing(
    percentage: Int,
    ringColor: Color,
    isDark: Boolean
) {
    val trackColor = if (isDark) Color.White.copy(alpha = 0.10f) else Color(0xFFD1D5DB)
    val innerBg    = if (isDark) Color(0xFF0B1B33) else Color(0xFFF8FAFC)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(170.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 13.dp.toPx()

            // Background track (full circle)
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = stroke,
                    cap = StrokeCap.Round
                )
            )

            // Foreground arc proportional to the score percentage
            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = 360f * (percentage / 100f),
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = stroke,
                    cap = StrokeCap.Round
                )
            )
        }

        // Center card showing the numeric percentage
        Box(
            modifier = Modifier
                .size(112.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(innerBg),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$percentage%",
                    color = ringColor,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Score",
                    color = if (isDark) DarkTextMuted else LightTextMuted,
                    fontSize = 12.sp
                )
            }
        }
    }
}

/**
 * Small icon + text badge used to display the quiz date and time taken.
 */
@Composable
private fun DetailBadge(
    icon: ImageVector,
    tint: Color,
    text: String,
    color: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(5.dp))
        Text(
            text = text,
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * Stat card showing either the correct count or mistake count.
 *
 * @param number    The numeric value to display prominently.
 * @param label     The label beneath the number (e.g. "Correct" or "Mistakes").
 * @param iconText  Symbol prefix shown next to the label ("✓" or "✕").
 * @param textColor Color applied to both the number and label text.
 * @param bgColor   Background color of the card.
 */
@Composable
private fun DetailStatCard(
    modifier: Modifier = Modifier,
    number: String,
    label: String,
    iconText: String,
    textColor: Color,
    bgColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = number,
                color = textColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = "$iconText  $label",
                color = textColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ── Preview helpers ───────────────────────────────────────────────────────────

/** Generates sample detail data for Compose previews. */
private fun sampleDetail(topic: String = "Programming", scoreText: String = "7/10") =
    QuizHistoryDetail(
        historyItem = QuizHistoryItem(
            id    = 4,
            topic = topic,
            date  = "20 Apr 2026",
            score = scoreText
        ),
        timeTaken      = "3:14",
        totalQuestions = 10,
        reviewItems    = listOf(
            ReviewQuestionItem("What does CPU stand for?",               "Central Processing Unit",    "Central Processing Unit",    true),
            ReviewQuestionItem("Which company developed Android?",       "Apple",                      "Google",                     false),
            ReviewQuestionItem("What is RAM mainly used for?",           "Temporary memory storage",   "Temporary memory storage",   true),
            ReviewQuestionItem("Which protocol secures web browsing?",   "HTTP",                       "HTTPS",                      false),
            ReviewQuestionItem("What does HTML stand for?",             "HyperText Markup Language",  "HyperText Markup Language",  true),
            ReviewQuestionItem("What is a compiler?",                    "Converts source to machine code", "Converts source to machine code", true),
            ReviewQuestionItem("Which data structure uses LIFO ordering?","Stack",                     "Stack",                      true),
            ReviewQuestionItem("What does OOP stand for?",               "Object-Oriented Programming","Object-Oriented Programming",true),
            ReviewQuestionItem("Which language runs natively in browsers?","Java",                     "JavaScript",                 false),
            ReviewQuestionItem("What does API stand for?",               "Application Programming Interface","Application Programming Interface",true)
        )
    )

@Preview(
    name = "Detail — Light",
    showBackground = true,
    backgroundColor = 0xFFF3F7FC,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true
)
@Composable
private fun HistoryDetailLightPreview() {
    StudentApplicationTheme {
        HistoryDetailScreen(detail = sampleDetail(), isDark = false)
    }
}

@Preview(
    name = "Detail — Dark",
    showBackground = true,
    backgroundColor = 0xFF000B1B,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true
)
@Composable
private fun HistoryDetailDarkPreview() {
    StudentApplicationTheme {
        HistoryDetailScreen(detail = sampleDetail(), isDark = true)
    }
}