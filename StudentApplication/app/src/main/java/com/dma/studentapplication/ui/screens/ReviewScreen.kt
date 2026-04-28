package com.dma.studentapplication.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dma.studentapplication.ui.screens.model.ReviewQuestionItem

// ── Main screen ───────────────────────────────────────────────────────────────

/**
 * Review screen shown after the user taps "Review Lesson" on the result screen.
 *
 * Displays:
 * - Screen title and topic name
 * - Summary card showing the overall score
 * - One [ReviewQuestionCard] per answered question
 * - Restart Quiz and Back to Home action buttons
 *
 * @param topicTitle         Display name of the quiz topic shown below the title.
 * @param score              Number of correct answers.
 * @param totalQuestions     Total number of questions in the quiz.
 * @param reviewItems        Per-question review data collected during the quiz.
 * @param onRestartQuizClick Called when the user wants to retake the same quiz.
 * @param onBackHomeClick    Called when the user wants to return to the home screen.
 */
@Composable
fun ReviewScreen(
    topicTitle: String,
    score: Int,
    totalQuestions: Int,
    reviewItems: List<ReviewQuestionItem>,
    onRestartQuizClick: () -> Unit = {},
    onBackHomeClick: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()

    // ── Theme-aware colors ────────────────────────────────────────────────────
    val screenBgTop    = if (isDark) Color(0xFF000B1B) else Color(0xFFF3F7FC)
    val screenBgBottom = if (isDark) Color(0xFF05162D) else Color(0xFFEAF2FB)
    val titleColor     = if (isDark) Color.White       else Color(0xFF0F172A)
    val subtitleColor  = if (isDark) Color(0xFFD6E0F5) else Color(0xFF64748B)
    val cardColor      = if (isDark) Color(0xFF08172E) else Color.White
    val borderColor    = if (isDark) Color(0xFF243B5A) else Color(0xFFDDE5EF)

    val primaryGreen = Color(0xFF16A34A)
    val accentTeal   = Color(0xFF0891B2)

    // Per-answer card backgrounds — green for correct, red for wrong
    val correctCardBg = if (isDark) Color(0xFF082A20) else Color(0xFFE8FFF3)
    val wrongCardBg   = if (isDark) Color(0xFF32131F) else Color(0xFFFFEEF1)

    // Per-answer border colors
    val correctBorder = if (isDark) Color(0xFF10B981) else Color(0xFF65D98D)
    val wrongBorder   = if (isDark) Color(0xFFFF6B8A) else Color(0xFFFF8FA3)

    // Per-answer text colors
    val correctTextColor = if (isDark) Color(0xFF6EE7B7) else Color(0xFF15803D)
    val wrongTextColor   = if (isDark) Color(0xFFFF9AA5) else Color(0xFFDC2626)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(screenBgTop, screenBgBottom)))
            .windowInsetsPadding(WindowInsets.safeDrawing),
        contentPadding = PaddingValues(
            start  = 20.dp,
            end    = 20.dp,
            top    = 34.dp,
            bottom = 28.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── Header: title, topic name, and score summary card ─────────────────
        item {
            Column(
                modifier            = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text       = "Review Lesson",
                    color      = titleColor,
                    fontSize   = 31.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign  = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Topic name shown in teal as a subtitle
                Text(
                    text       = topicTitle,
                    color      = accentTeal,
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign  = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Score summary card — shows "correct / total" prominently
                Card(
                    modifier  = Modifier.fillMaxWidth(),
                    shape     = RoundedCornerShape(28.dp),
                    colors    = CardDefaults.cardColors(containerColor = cardColor),
                    border    = BorderStroke(1.dp, borderColor),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(
                        modifier            = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text       = "$score / $totalQuestions",
                            color      = titleColor,
                            fontSize   = 42.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text       = "Here is a review of your quiz answers",
                            color      = subtitleColor,
                            fontSize   = 16.sp,
                            textAlign  = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }

        // ── Per-question review cards — one per answered question ─────────────
        itemsIndexed(reviewItems) { index, item ->
            ReviewQuestionCard(
                questionNumber   = index + 1,
                item             = item,
                isDark           = isDark,
                titleColor       = titleColor,
                subtitleColor    = subtitleColor,
                correctCardBg    = correctCardBg,
                wrongCardBg      = wrongCardBg,
                correctBorder    = correctBorder,
                wrongBorder      = wrongBorder,
                correctTextColor = correctTextColor,
                wrongTextColor   = wrongTextColor
            )
        }

        // ── Action buttons ────────────────────────────────────────────────────
        item {
            Column(
                modifier            = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Primary action — retake the same quiz
                Button(
                    onClick  = onRestartQuizClick,
                    modifier = Modifier.fillMaxWidth().height(58.dp),
                    shape    = RoundedCornerShape(20.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = primaryGreen,
                        contentColor   = Color.White
                    )
                ) {
                    Icon(
                        imageVector        = Icons.Default.Refresh,
                        contentDescription = "Restart Quiz",
                        modifier           = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Restart Quiz", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                }

                // Secondary action — return to home screen
                OutlinedButton(
                    onClick  = onBackHomeClick,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape    = RoundedCornerShape(20.dp),
                    border   = BorderStroke(
                        1.5.dp,
                        if (isDark) Color(0xFF334155) else Color(0xFFCBD5E1)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isDark) Color(0xFF0B1F3A) else Color.White,
                        contentColor   = titleColor
                    )
                ) {
                    Icon(
                        imageVector        = Icons.Default.Home,
                        contentDescription = "Back Home",
                        modifier           = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Back to Home", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}

// ── Review question card ──────────────────────────────────────────────────────

/**
 * Card displaying the result for a single quiz question.
 *
 * Shows:
 * - Status icon (✓ or ✗) and "Question N" label
 * - The question text
 * - The user's selected answer
 * - If wrong: a divider followed by the correct answer
 *
 * Card background, border, and text colors all adapt based on [item.isCorrect].
 *
 * @param questionNumber     1-based question number shown in the header row.
 * @param item               The review data for this question.
 * @param isDark             Whether dark mode is active (affects divider color).
 * @param titleColor         Color used for the question text.
 * @param subtitleColor      Color used for "Your Answer" / "Correct Answer" labels.
 * @param correctCardBg      Background color when the answer was correct.
 * @param wrongCardBg        Background color when the answer was wrong.
 * @param correctBorder      Border color when the answer was correct.
 * @param wrongBorder        Border color when the answer was wrong.
 * @param correctTextColor   Text color used for correct answer content.
 * @param wrongTextColor     Text color used for wrong answer content.
 */
@Composable
fun ReviewQuestionCard(
    questionNumber: Int,
    item: ReviewQuestionItem,
    isDark: Boolean,
    titleColor: Color,
    subtitleColor: Color,
    correctCardBg: Color,
    wrongCardBg: Color,
    correctBorder: Color,
    wrongBorder: Color,
    correctTextColor: Color,
    wrongTextColor: Color
) {
    // Resolve colors based on whether the answer was correct
    val containerColor      = if (item.isCorrect) correctCardBg    else wrongCardBg
    val borderColor         = if (item.isCorrect) correctBorder     else wrongBorder
    val statusColor         = if (item.isCorrect) correctTextColor  else wrongTextColor
    val selectedAnswerColor = if (item.isCorrect) correctTextColor  else wrongTextColor

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(26.dp),
        colors    = CardDefaults.cardColors(containerColor = containerColor),
        border    = BorderStroke(1.5.dp, borderColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        ) {
            // Status row — check/cancel icon + "Question N" label
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (item.isCorrect) Icons.Default.CheckCircle
                    else                Icons.Default.Cancel,
                    contentDescription = if (item.isCorrect) "Correct" else "Wrong",
                    tint     = statusColor,
                    modifier = Modifier.size(26.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text       = "Question $questionNumber",
                    color      = statusColor,
                    fontSize   = 17.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Question text
            Text(
                text       = item.question,
                color      = titleColor,
                fontSize   = 20.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // User's selected answer
            Text(
                text       = "Your Answer",
                color      = subtitleColor,
                fontSize   = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text       = item.selectedAnswer,
                color      = selectedAnswerColor,
                fontSize   = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 25.sp
            )

            // Correct answer section — only shown when the user answered incorrectly
            if (!item.isCorrect) {
                Spacer(modifier = Modifier.height(18.dp))

                // Divider separates the wrong answer from the correct answer reveal
                HorizontalDivider(
                    color = if (isDark) Color.White.copy(alpha = 0.12f) else Color(0xFFDDE5EF)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text       = "Correct Answer",
                    color      = subtitleColor,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text       = item.correctAnswer,
                    color      = correctTextColor,
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 25.sp
                )
            }
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(
    name            = "Review Light",
    showBackground  = true,
    backgroundColor = 0xFFF3F7FC,
    uiMode          = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi    = true
)
@Composable
private fun ReviewScreenLightPreview() {
    ReviewScreen(
        topicTitle     = "Technology Quiz",
        score          = 7,
        totalQuestions = 10,
        reviewItems    = sampleReviewItems()
    )
}

@Preview(
    name            = "Review Dark",
    showBackground  = true,
    backgroundColor = 0xFF000B1B,
    uiMode          = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi    = true
)
@Composable
private fun ReviewScreenDarkPreview() {
    ReviewScreen(
        topicTitle     = "Technology Quiz",
        score          = 7,
        totalQuestions = 10,
        reviewItems    = sampleReviewItems()
    )
}

// ── Preview data ──────────────────────────────────────────────────────────────

/** Sample review items used exclusively for Compose previews. */
private fun sampleReviewItems(): List<ReviewQuestionItem> = listOf(
    ReviewQuestionItem(
        question       = "What does CPU stand for?",
        selectedAnswer = "Central Processing Unit",
        correctAnswer  = "Central Processing Unit",
        isCorrect      = true
    ),
    ReviewQuestionItem(
        question       = "Which company developed Android?",
        selectedAnswer = "Apple",
        correctAnswer  = "Google",
        isCorrect      = false
    ),
    ReviewQuestionItem(
        question       = "What is the main purpose of RAM?",
        selectedAnswer = "Temporary memory storage",
        correctAnswer  = "Temporary memory storage",
        isCorrect      = true
    ),
    ReviewQuestionItem(
        question       = "Which protocol secures web browsing?",
        selectedAnswer = "HTTP",
        correctAnswer  = "HTTPS",
        isCorrect      = false
    )
)