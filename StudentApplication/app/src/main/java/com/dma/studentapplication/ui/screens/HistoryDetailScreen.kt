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

// ─── Shared color tokens (from HistoryScreen / HomeScreen) ───────────────────

private val PrimaryGreen      = Color(0xFF27D17F)
private val PrimaryGreenAlpha = Color(0x1A27D17F)
private val AccentTeal        = Color(0xFF16C6D9)

private val LightBackground   = Color(0xFFF3F4F6)
private val LightCard         = Color(0xFFFFFFFF)
private val LightBorder       = Color(0xFFE6ECF2)
private val LightTextDark     = Color(0xFF0B1B4A)
private val LightTextMuted    = Color(0xFF5B6785)

private val DarkBackground    = Color(0xFF000B1B)
private val DarkCard          = Color(0xFF071833)
private val DarkBorder        = Color(0xFF1A2A40)
private val DarkTextDark      = Color(0xFFFFFFFF)
private val DarkTextMuted     = Color(0xFF8FA3C8)

// ─── Topic icon meta (same map as HistoryScreen) ─────────────────────────────

private data class DetailTopicMeta(
    val icon: ImageVector,
    val tint: Color,
    val lightBg: Color,
    val darkBg: Color
)

private val detailTopicMeta: Map<String, DetailTopicMeta> = mapOf(
    "Math"        to DetailTopicMeta(Icons.Default.Calculate,        Color(0xFF22C55E), Color(0xFFEAF8EE), Color(0xFF0C2540)),
    "History"     to DetailTopicMeta(Icons.Outlined.AutoStories,     Color(0xFF4B83FF), Color(0xFFEEF4FF), Color(0xFF10284A)),
    "Science"     to DetailTopicMeta(Icons.Default.Science,          Color(0xFF8B5CF6), Color(0xFFF3EDFF), Color(0xFF1A234B)),
    "Programming" to DetailTopicMeta(Icons.Default.Code,             Color(0xFF10CBE8), Color(0xFFEAFBFF), Color(0xFF102848)),
    "Movies"      to DetailTopicMeta(Icons.Default.LocalMovies,      Color(0xFFF45B87), Color(0xFFFFEEF3), Color(0xFF2A1C3F)),
    "Sports"      to DetailTopicMeta(Icons.Default.SportsBasketball, Color(0xFFF59E0B), Color(0xFFFFF5E6), Color(0xFF3A2810)),
    "Geography"   to DetailTopicMeta(Icons.Default.Public,           Color(0xFF14B87A), Color(0xFFEAFBF5), Color(0xFF102A2B)),
)

// ─── Data model ───────────────────────────────────────────────────────────────

// ─── Screen ───────────────────────────────────────────────────────────────────

/**
 * Detail screen for a single past quiz session, reached by tapping a card
 * in [HistoryScreen].
 *
 * Layout mirrors [ReviewScreen] (scrollable question cards + sticky bottom
 * actions) while the header/ring mirrors [ResultScreen].
 *
 * @param detail            Full detail data for this quiz session.
 * @param isDark            Whether dark mode is active; defaults to system setting.
 * @param onBackClick       Called when the back arrow is tapped.
 * @param onRetakeQuizClick Called when the user wants to retake this quiz.
 * @param onBackHomeClick   Called when the user navigates back to [HomeScreen].
 */
@Composable
fun HistoryDetailScreen(
    detail: QuizHistoryDetail,
    isDark: Boolean = isSystemInDarkTheme(),
    onBackClick: () -> Unit = {},
    onRetakeQuizClick: () -> Unit = {},
    onBackHomeClick: () -> Unit = {}
) {
    // ── Resolve colors ────────────────────────────────────────────────────────
    val screenBgTop    = if (isDark) DarkBackground         else Color(0xFFF3F7FC)
    val screenBgBottom = if (isDark) Color(0xFF05162D)      else Color(0xFFEAF2FB)
    val cardColor      = if (isDark) DarkCard               else LightCard
    val borderColor    = if (isDark) DarkBorder             else LightBorder
    val textDark       = if (isDark) DarkTextDark           else LightTextDark
    val textMuted      = if (isDark) DarkTextMuted          else LightTextMuted

    val correctCardBg    = if (isDark) Color(0xFF10271F)    else Color(0xFFECFDF5)
    val wrongCardBg      = if (isDark) Color(0xFF2B1520)    else Color(0xFFFFF1F2)
    val correctBorder    = if (isDark) Color(0xFF1F8F63)    else Color(0xFF86EFAC)
    val wrongBorder      = if (isDark) Color(0xFFB94A64)    else Color(0xFFFDA4AF)
    val correctTextColor = if (isDark) Color(0xFF6EE7B7)    else Color(0xFF15803D)
    val wrongTextColor   = if (isDark) Color(0xFFFF9AA5)    else Color(0xFFDC2626)

    // ── Derived values ────────────────────────────────────────────────────────
    val scoreText  = detail.historyItem.score           // e.g. "8/10"
    val scoreParts = scoreText.split("/")
    val correct    = scoreParts.getOrNull(0)?.trim()?.toIntOrNull() ?: 0
    val total      = scoreParts.getOrNull(1)?.trim()?.toIntOrNull()
        ?.takeIf { it > 0 } ?: detail.totalQuestions.coerceAtLeast(1)
    val percentage = ((correct.toFloat() / total.toFloat()) * 100).toInt()
    val mistakes   = (total - correct).coerceAtLeast(0)

    val ringColor = when {
        percentage >= 80 -> Color(0xFF22C55E)
        percentage >= 50 -> Color(0xFFF59E0B)
        else             -> Color(0xFFEF4444)
    }

    val meta = detailTopicMeta[detail.historyItem.topic]

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(screenBgTop, screenBgBottom)))
                .padding(WindowInsets.navigationBars.asPaddingValues())
        ) {

            // ── Scrollable body ───────────────────────────────────────────────
            LazyColumn(
                modifier       = Modifier.weight(1f),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 16.dp)
            ) {

                // Top bar: back arrow + topic name
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

                // Score ring + stat row
                item {
                    Column(
                        modifier            = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(8.dp))

                        // Score ring — same Canvas approach as ResultScreen
                        DetailScoreRing(
                            percentage = percentage,
                            ringColor  = ringColor,
                            isDark     = isDark
                        )

                        Spacer(Modifier.height(20.dp))

                        // Date + time row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            // Date badge
                            DetailBadge(
                                icon  = Icons.Default.EmojiEvents,
                                tint  = PrimaryGreen,
                                text  = detail.historyItem.date,
                                color = textMuted
                            )

                            // Time taken badge
                            DetailBadge(
                                icon  = Icons.Default.AccessTime,
                                tint  = Color(0xFFEAB308),
                                text  = detail.timeTaken,
                                color = Color(0xFFEAB308)
                            )
                        }

                        Spacer(Modifier.height(20.dp))

                        // Correct / Mistakes stat cards — mirrors ResultScreen StatCard pair
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            DetailStatCard(
                                modifier    = Modifier.weight(1f),
                                number      = correct.toString(),
                                label       = "Correct",
                                iconText    = "✓",
                                textColor   = correctTextColor,
                                bgColor     = correctCardBg
                            )
                            DetailStatCard(
                                modifier    = Modifier.weight(1f),
                                number      = mistakes.toString(),
                                label       = "Mistakes",
                                iconText    = "✕",
                                textColor   = wrongTextColor,
                                bgColor     = wrongCardBg
                            )
                        }

                        Spacer(Modifier.height(24.dp))

                        // "Answer breakdown" section label — mirrors HomeScreen SectionTitle
                        Row(
                            modifier          = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector        = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint               = PrimaryGreen,
                                modifier           = Modifier.size(20.dp)
                            )
                            Text(
                                text       = "Answer Breakdown",
                                color      = textDark,
                                fontSize   = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier   = Modifier.padding(start = 10.dp)
                            )
                        }

                        Spacer(Modifier.height(4.dp))
                    }
                }

                // Per-question review cards — same component as ReviewScreen
                itemsIndexed(detail.reviewItems) { index, item ->
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

                item { Spacer(Modifier.height(8.dp)) }
            }

            // ── Sticky bottom actions — mirrors ReviewScreen button section ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Primary CTA: retake
                Button(
                    onClick  = onRetakeQuizClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape  = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen,
                        contentColor   = if (isDark) Color(0xFF052012) else Color.White
                    )
                ) {
                    Icon(
                        imageVector        = Icons.Default.Refresh,
                        contentDescription = "Retake Quiz",
                        modifier           = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = "Retake Quiz", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                // Secondary CTA: back home
                OutlinedButton(
                    onClick  = onBackHomeClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape  = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, borderColor),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = textDark)
                ) {
                    Icon(
                        imageVector        = Icons.Default.Home,
                        contentDescription = "Back Home",
                        modifier           = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = "Back Home", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// ─── Top bar ──────────────────────────────────────────────────────────────────

/**
 * Back arrow on the left + topic icon bubble + topic name centred.
 * Mirrors the QuizScreen top bar structure.
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
        modifier          = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button — same as QuizScreen
        IconButton(onClick = onBack) {
            Icon(
                imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint               = textDark,
                modifier           = Modifier.size(24.dp)
            )
        }

        // Topic icon + name — centred in remaining space
        Row(
            modifier          = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Topic icon bubble — identical to HistoryCard bubble
            if (meta != null) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isDark) meta.darkBg else meta.lightBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = meta.icon,
                        contentDescription = topic,
                        tint               = meta.tint,
                        modifier           = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(10.dp))
            }
            Column {
                Text(
                    text       = topic,
                    color      = textDark,
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text     = "Quiz Details",
                    color    = AccentTeal,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Invisible placeholder to keep topic centred
        Spacer(Modifier.size(48.dp))
    }
}

// ─── Score ring ───────────────────────────────────────────────────────────────

/**
 * Circular progress ring with percentage text — same Canvas pattern as
 * [ResultScreen] ScoreRing but without the RoboBuddy (this is a history
 * review, not an active result celebration).
 */
@Composable
private fun DetailScoreRing(
    percentage: Int,
    ringColor: Color,
    isDark: Boolean
) {
    val trackColor = if (isDark) Color.White.copy(alpha = 0.10f) else Color(0xFFD1D5DB)
    val innerBg    = if (isDark) Color(0xFF0B1B33)               else Color(0xFFF8FAFC)

    Box(
        contentAlignment = Alignment.Center,
        modifier         = Modifier.size(170.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 13.dp.toPx()

            // Track
            drawArc(
                color      = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter  = false,
                style      = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke, cap = StrokeCap.Round)
            )
            // Progress
            drawArc(
                color      = ringColor,
                startAngle = -90f,
                sweepAngle = 360f * (percentage / 100f),
                useCenter  = false,
                style      = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }

        // Inner pill
        Box(
            modifier         = Modifier
                .size(112.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(innerBg),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text       = "$percentage%",
                    color      = ringColor,
                    fontSize   = 26.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text     = "Score",
                    color    = if (isDark) DarkTextMuted else LightTextMuted,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// ─── Small badge (date / time) ────────────────────────────────────────────────

@Composable
private fun DetailBadge(
    icon: ImageVector,
    tint: Color,
    text: String,
    color: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector        = icon,
            contentDescription = null,
            tint               = tint,
            modifier           = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(5.dp))
        Text(text = text, color = color, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

// ─── Stat card (correct / mistakes) ──────────────────────────────────────────

/**
 * Mirrors [ResultScreen]'s private StatCard exactly.
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
        shape    = RoundedCornerShape(20.dp),
        colors   = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(number, color = textColor, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(5.dp))
            Text("$iconText  $label", color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

private fun sampleDetail(topic: String = "Programming", scoreText: String = "7/10") =
    QuizHistoryDetail(
        historyItem = QuizHistoryItem(id = 4, topic = topic, date = "20 Apr 2026", score = scoreText),
        timeTaken = "3:14",
        totalQuestions = 10,
        reviewItems = listOf(
            ReviewQuestionItem("What does CPU stand for?",                   "Central Processing Unit", "Central Processing Unit", true),
            ReviewQuestionItem("Which company developed Android?",           "Apple",                   "Google",                  false),
            ReviewQuestionItem("What is RAM mainly used for?",               "Temporary memory storage","Temporary memory storage",true),
            ReviewQuestionItem("Which protocol secures web browsing?",       "HTTP",                    "HTTPS",                   false),
            ReviewQuestionItem("What does HTML stand for?",                  "HyperText Markup Language","HyperText Markup Language",true),
            ReviewQuestionItem("What is a compiler?",                        "Converts source to machine code","Converts source to machine code",true),
            ReviewQuestionItem("Which data structure uses LIFO ordering?",   "Stack",                   "Stack",                   true),
            ReviewQuestionItem("What does OOP stand for?",                   "Object-Oriented Programming","Object-Oriented Programming",true),
            ReviewQuestionItem("Which language runs natively in browsers?",  "Java",                    "JavaScript",              false),
            ReviewQuestionItem("What does API stand for?",                   "Application Programming Interface","Application Programming Interface",true),
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

@Preview(
    name = "Detail — Math (high score)",
    showBackground = true,
    backgroundColor = 0xFFF3F7FC,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true
)
@Composable
private fun HistoryDetailMathPreview() {
    StudentApplicationTheme {
        HistoryDetailScreen(detail = sampleDetail(topic = "Math", scoreText = "10/10"), isDark = false)
    }
}

@Preview(
    name = "Detail — Science (low score, dark)",
    showBackground = true,
    backgroundColor = 0xFF000B1B,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true
)
@Composable
private fun HistoryDetailScienceDarkPreview() {
    StudentApplicationTheme {
        HistoryDetailScreen(detail = sampleDetail(topic = "Science", scoreText = "3/10"), isDark = true)
    }
}