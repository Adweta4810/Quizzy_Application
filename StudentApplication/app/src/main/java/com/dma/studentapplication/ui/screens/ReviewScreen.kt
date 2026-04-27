package com.dma.studentapplication.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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

    val screenBgTop    = if (isDark) Color(0xFF000B1B) else Color(0xFFF3F7FC)
    val screenBgBottom = if (isDark) Color(0xFF05162D) else Color(0xFFEAF2FB)
    val titleColor     = if (isDark) Color.White       else Color(0xFF111827)
    val subtitleColor  = if (isDark) Color(0xFFB7C4D8) else Color(0xFF6B7280)
    val borderColor    = if (isDark) Color.White.copy(alpha = 0.08f) else Color(0xFFE5E7EB)
    val cardColor      = if (isDark) Color(0xFF08172E)  else Color.White
    val primaryColor   = Color(0xFF27D17F)
    val accentTeal     = Color(0xFF16C6D9)

    val correctCardBg    = if (isDark) Color(0xFF10271F) else Color(0xFFECFDF5)
    val wrongCardBg      = if (isDark) Color(0xFF2B1520) else Color(0xFFFFF1F2)
    val correctBorder    = if (isDark) Color(0xFF1F8F63) else Color(0xFF86EFAC)
    val wrongBorder      = if (isDark) Color(0xFFB94A64) else Color(0xFFFDA4AF)
    val correctTextColor = if (isDark) Color(0xFF6EE7B7) else Color(0xFF15803D)
    val wrongTextColor   = if (isDark) Color(0xFFFF9AA5) else Color(0xFFDC2626)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(screenBgTop, screenBgBottom)))
            .windowInsetsPadding(WindowInsets.safeDrawing),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 30.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Review Lesson", color = titleColor, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = topicTitle, color = accentTeal, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(14.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(22.dp),
                    colors   = CardDefaults.cardColors(containerColor = cardColor),
                    border   = BorderStroke(1.dp, borderColor)
                ) {
                    Column(
                        modifier            = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "$score / $totalQuestions", color = titleColor, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Here is a review of your quiz answers", color = subtitleColor, fontSize = 14.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        }

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

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick  = onRestartQuizClick,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape    = RoundedCornerShape(18.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = primaryColor,
                        contentColor   = if (isDark) Color(0xFF052012) else Color.White
                    )
                ) {
                    Icon(Icons.Default.Refresh, "Restart Quiz", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Restart Quiz", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick  = onBackHomeClick,
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape    = RoundedCornerShape(18.dp),
                    border   = BorderStroke(1.dp, borderColor),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = titleColor)
                ) {
                    Icon(Icons.Default.Home, "Back Home", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Back Home", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// ─── Public so HistoryDetailScreen can reuse it ───────────────────────────────

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
    val containerColor   = if (item.isCorrect) correctCardBg    else wrongCardBg
    val borderColor      = if (item.isCorrect) correctBorder     else wrongBorder
    val statusTextColor  = if (item.isCorrect) correctTextColor  else wrongTextColor
    val answerLabelColor = if (item.isCorrect) correctTextColor  else wrongTextColor

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(20.dp),
        colors   = CardDefaults.cardColors(containerColor = containerColor),
        border   = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector        = if (item.isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    contentDescription = if (item.isCorrect) "Correct" else "Wrong",
                    tint               = statusTextColor,
                    modifier           = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Question $questionNumber", color = statusTextColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = item.question, color = titleColor, fontSize = 17.sp, fontWeight = FontWeight.SemiBold, lineHeight = 24.sp)
            Spacer(modifier = Modifier.height(14.dp))
            Text(text = "Your Answer", color = subtitleColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.selectedAnswer, color = answerLabelColor, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, lineHeight = 22.sp)

            if (!item.isCorrect) {
                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = if (isDark) Color.White.copy(alpha = 0.08f) else Color(0xFFE5E7EB))
                Spacer(modifier = Modifier.height(14.dp))
                Text(text = "Correct Answer", color = subtitleColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = item.correctAnswer, color = correctTextColor, fontSize = 15.sp, fontWeight = FontWeight.Bold, lineHeight = 22.sp)
            }
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(name = "Review Light", showBackground = true, backgroundColor = 0xFFFFFFFF, uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true)
@Composable
private fun ReviewScreenLightPreview() {
    ReviewScreen(topicTitle = "Technology Quiz", score = 7, totalQuestions = 10, reviewItems = sampleReviewItems())
}

@Preview(name = "Review Dark", showBackground = true, backgroundColor = 0xFF000B1B, uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun ReviewScreenDarkPreview() {
    ReviewScreen(topicTitle = "Technology Quiz", score = 7, totalQuestions = 10, reviewItems = sampleReviewItems())
}

private fun sampleReviewItems(): List<ReviewQuestionItem> = listOf(
    ReviewQuestionItem("What does CPU stand for?",                "Central Processing Unit", "Central Processing Unit", true),
    ReviewQuestionItem("Which company developed Android?",        "Apple",                   "Google",                  false),
    ReviewQuestionItem("What is the main purpose of RAM?",        "Temporary memory storage","Temporary memory storage",true),
    ReviewQuestionItem("Which protocol secures web browsing?",    "HTTP",                    "HTTPS",                   false),
    ReviewQuestionItem("What does HTML stand for?",               "HyperText Markup Language","HyperText Markup Language",true)
)
