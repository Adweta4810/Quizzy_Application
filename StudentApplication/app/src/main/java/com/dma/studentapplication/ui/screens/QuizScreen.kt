package com.dma.studentapplication.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.dma.studentapplication.ui.model.QuizQuestionUi
import com.dma.studentapplication.ui.theme.StudentApplicationTheme
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(
    questions: List<QuizQuestionUi>,
    topicTitle: String = "Programming Quiz",
    onBackClick: () -> Unit = {},
    onQuizFinished: (
        score: Int,
        total: Int,
        reviewItems: List<ReviewQuestionItem>
    ) -> Unit = { _, _, _ -> }
) {
    val isDark         = isSystemInDarkTheme()
    val totalQuestions = questions.size

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswerIndex  by remember { mutableStateOf<Int?>(null) }
    var answerLocked         by remember { mutableStateOf(false) }
    var score                by remember { mutableIntStateOf(0) }
    var timeLeft             by remember(currentQuestionIndex) { mutableIntStateOf(15) }

    val reviewItems     = remember { mutableStateListOf<ReviewQuestionItem>() }
    val currentQuestion = questions.getOrNull(currentQuestionIndex)

    LaunchedEffect(currentQuestionIndex, answerLocked) {
        if (!answerLocked) {
            timeLeft = 15
            while (timeLeft > 0 && !answerLocked) {
                delay(1000)
                timeLeft--
            }
            if (timeLeft == 0 && !answerLocked) {
                currentQuestion?.let {
                    reviewItems.add(
                        ReviewQuestionItem(
                            question       = it.question,
                            selectedAnswer = "No answer selected",
                            correctAnswer  = it.correctAnswer,
                            isCorrect      = false
                        )
                    )
                }
                answerLocked = true
            }
        }
    }

    if (currentQuestion == null) {
        onQuizFinished(score, totalQuestions, reviewItems.toList())
        return
    }

    val correctIndex = currentQuestion.options.indexOf(currentQuestion.correctAnswer)

    val roboState = when {
        answerLocked && selectedAnswerIndex == correctIndex                                -> RoboBuddyState.CORRECT
        answerLocked && selectedAnswerIndex != null && selectedAnswerIndex != correctIndex -> RoboBuddyState.INCORRECT
        answerLocked && selectedAnswerIndex == null                                       -> RoboBuddyState.SAD
        timeLeft <= 5                                                                     -> RoboBuddyState.SURPRISED
        else                                                                              -> RoboBuddyState.THINKING
    }

    val screenBg        = if (isDark) Color(0xFF000B1B) else Color(0xFFF3F7FC)
    val screenBgBottom  = if (isDark) Color(0xFF05162D) else Color(0xFFEAF2FB)
    val surfaceColor    = if (isDark) Color(0xFF071833) else Color.White
    val cardBorder      = if (isDark) Color.White.copy(alpha = 0.08f) else Color(0xFFE2E8F0)
    val titleColor      = if (isDark) Color.White       else Color(0xFF0F172A)
    val subtitleColor   = if (isDark) Color(0xFFB8C4E0) else Color(0xFF64748B)
    val accentGreen     = Color(0xFF27D17F)
    val accentTeal      = Color(0xFF16C6D9)
    val warningColor    = Color(0xFFFFB84D)
    val wrongColor      = Color(0xFFFF6B6B)
    val buttonTextColor = if (isDark) Color(0xFF052012) else Color.White

    val animatedProgress by animateFloatAsState(
        targetValue   = (currentQuestionIndex + 1) / totalQuestions.toFloat(),
        animationSpec = tween(600),
        label         = "progress"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(screenBg, screenBgBottom)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.navigationBars.asPaddingValues())
        ) {
            // ── Sticky top bar ────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier          = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier        = Modifier.size(42.dp),
                        shape           = CircleShape,
                        color           = surfaceColor,
                        tonalElevation  = 0.dp,
                        shadowElevation = if (isDark) 0.dp else 2.dp,
                        border          = BorderStroke(1.dp, cardBorder)
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = titleColor)
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(topicTitle, color = titleColor, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, maxLines = 1)
                        Text("Question ${currentQuestionIndex + 1} of $totalQuestions", color = subtitleColor, fontSize = 13.sp)
                    }

                    ScoreChip(score = score, isDark = isDark, accentGreen = accentGreen)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier          = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LinearProgressIndicator(
                        progress      = { animatedProgress },
                        modifier      = Modifier.weight(1f).height(8.dp).clip(RoundedCornerShape(20.dp)),
                        color         = accentGreen,
                        trackColor    = if (isDark) Color.White.copy(alpha = 0.10f) else Color(0xFFE5E7EB),
                        strokeCap     = StrokeCap.Round
                    )
                    TimerChip(timeLeft = timeLeft, accentGreen = accentGreen, warningColor = warningColor, wrongColor = wrongColor)
                }
            }

            // ── Scrollable body ───────────────────────────────────────────────
            LazyColumn(
                modifier       = Modifier.weight(1f),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // RoboBuddy card
                item(key = "robo") {
                    Card(
                        modifier  = Modifier.fillMaxWidth(),
                        shape     = RoundedCornerShape(24.dp),
                        colors    = CardDefaults.cardColors(containerColor = Color.Transparent),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.horizontalGradient(listOf(Color(0xFF14838A), Color(0xFF25D39F)))
                                )
                                .padding(vertical = 16.dp, horizontal = 16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                RoboBuddy(state = roboState, modifier = Modifier.size(90.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = when {
                                            answerLocked && selectedAnswerIndex == correctIndex                                -> "Nice one!"
                                            answerLocked && selectedAnswerIndex != null && selectedAnswerIndex != correctIndex -> "Oops, not this time"
                                            answerLocked && selectedAnswerIndex == null                                       -> "Time is up!"
                                            timeLeft <= 5                                                                     -> "Hurry up!"
                                            else                                                                              -> "Think carefully!"
                                        },
                                        color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = when {
                                            answerLocked && selectedAnswerIndex == correctIndex                                -> "Robo is proud of you!"
                                            answerLocked && selectedAnswerIndex != null && selectedAnswerIndex != correctIndex -> "Check the correct answer below."
                                            answerLocked && selectedAnswerIndex == null                                       -> "You ran out of time."
                                            else                                                                              -> "You have ${timeLeft}s for this question."
                                        },
                                        color = Color.White.copy(alpha = 0.90f), fontSize = 12.sp, lineHeight = 17.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Question card
                item(key = "question_${currentQuestionIndex}") {
                    Card(
                        modifier  = Modifier.fillMaxWidth(),
                        shape     = RoundedCornerShape(24.dp),
                        colors    = CardDefaults.cardColors(containerColor = surfaceColor),
                        border    = BorderStroke(1.dp, cardBorder),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Surface(shape = RoundedCornerShape(8.dp), color = accentTeal.copy(alpha = 0.12f)) {
                                Text(
                                    text     = "Question ${currentQuestionIndex + 1}",
                                    color    = accentTeal,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text       = currentQuestion.question,
                                color      = titleColor,
                                fontSize   = 20.sp,
                                lineHeight = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Answer options
                items(
                    items = currentQuestion.options.mapIndexed { i, opt -> i to opt },
                    key   = { (i, _) -> "option_${currentQuestionIndex}_$i" }
                ) { (index, option) ->
                    QuizOptionItem(
                        optionLabel     = ('A' + index).toString(),
                        optionText      = option,
                        isSelected      = selectedAnswerIndex == index,
                        isCorrect       = answerLocked && index == correctIndex,
                        isWrongSelected = answerLocked && selectedAnswerIndex == index && index != correctIndex,
                        enabled         = !answerLocked,
                        isDark          = isDark,
                        onClick         = { if (!answerLocked) selectedAnswerIndex = index }
                    )
                }

                // Correct answer reveal (only when wrong answer locked)
                if (answerLocked && selectedAnswerIndex != correctIndex) {
                    item(key = "reveal") {
                        CorrectAnswerReveal(correctAnswer = currentQuestion.correctAnswer, isDark = isDark)
                    }
                }
            }

            // ── Sticky bottom button ──────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = {
                        if (!answerLocked) {
                            val isCorrect = selectedAnswerIndex == correctIndex
                            if (isCorrect) score++
                            reviewItems.add(
                                ReviewQuestionItem(
                                    question       = currentQuestion.question,
                                    selectedAnswer = selectedAnswerIndex
                                        ?.let { currentQuestion.options[it] } ?: "No answer selected",
                                    correctAnswer  = currentQuestion.correctAnswer,
                                    isCorrect      = isCorrect
                                )
                            )
                            answerLocked = true
                        } else {
                            if (currentQuestionIndex < totalQuestions - 1) {
                                currentQuestionIndex++
                                selectedAnswerIndex = null
                                answerLocked        = false
                            } else {
                                onQuizFinished(score, totalQuestions, reviewItems.toList())
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape    = RoundedCornerShape(18.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor         = accentGreen,
                        contentColor           = buttonTextColor,
                        disabledContainerColor = accentGreen.copy(alpha = 0.35f),
                        disabledContentColor   = Color.White.copy(alpha = 0.5f)
                    ),
                    enabled = selectedAnswerIndex != null || answerLocked
                ) {
                    Text(
                        text = when {
                            !answerLocked                               -> "Lock Answer"
                            currentQuestionIndex == totalQuestions - 1 -> "Finish Quiz"
                            else                                        -> "Next Question"
                        },
                        fontSize = 16.sp, fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
private fun CorrectAnswerReveal(correctAnswer: String, isDark: Boolean) {
    val bg     = if (isDark) Color(0xFF10271F) else Color(0xFFECFDF5)
    val border = if (isDark) Color(0xFF1F8F63) else Color(0xFF86EFAC)
    val text   = if (isDark) Color(0xFF6EE7B7) else Color(0xFF15803D)
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(18.dp),
        colors    = CardDefaults.cardColors(containerColor = bg),
        border    = BorderStroke(1.dp, border),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier          = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.CheckCircle, null, tint = text, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text("Correct Answer", color = text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Text(correctAnswer, color = text, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 21.sp)
            }
        }
    }
}

@Composable
private fun ScoreChip(score: Int, isDark: Boolean, accentGreen: Color) {
    Surface(
        shape  = RoundedCornerShape(14.dp),
        color  = accentGreen.copy(alpha = if (isDark) 0.18f else 0.12f),
        border = BorderStroke(1.dp, accentGreen.copy(alpha = 0.35f))
    ) {
        Text(
            text       = "Score $score",
            modifier   = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            color      = if (isDark) Color(0xFF6EE7B7) else Color(0xFF166534),
            fontSize   = 14.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun TimerChip(timeLeft: Int, accentGreen: Color, warningColor: Color, wrongColor: Color) {
    val timerColor by animateColorAsState(
        targetValue   = when { timeLeft <= 3 -> wrongColor; timeLeft <= 5 -> warningColor; else -> accentGreen },
        animationSpec = tween(300),
        label         = "timerColor"
    )
    Surface(
        shape  = RoundedCornerShape(14.dp),
        color  = timerColor.copy(alpha = 0.16f),
        border = BorderStroke(1.dp, timerColor.copy(alpha = 0.40f))
    ) {
        Row(
            modifier          = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Timer, "Timer", tint = timerColor, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("${timeLeft}s", color = timerColor, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
        }
    }
}

@Composable
private fun QuizOptionItem(
    optionLabel: String,
    optionText: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isWrongSelected: Boolean,
    enabled: Boolean,
    isDark: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue   = when {
            isCorrect       -> if (isDark) Color(0xFF123A2B) else Color(0xFFEAFBF1)
            isWrongSelected -> if (isDark) Color(0xFF3A1A23) else Color(0xFFFFF1F1)
            isSelected      -> if (isDark) Color(0xFF102B4A) else Color(0xFFE8F4FF)
            else            -> if (isDark) Color(0xFF071833) else Color.White
        },
        animationSpec = tween(200), label = "optionBg"
    )
    val borderColor by animateColorAsState(
        targetValue   = when {
            isCorrect       -> Color(0xFF27D17F)
            isWrongSelected -> Color(0xFFFF6B6B)
            isSelected      -> Color(0xFF16C6D9)
            else            -> if (isDark) Color.White.copy(alpha = 0.10f) else Color(0xFFE2E8F0)
        },
        animationSpec = tween(200), label = "optionBorder"
    )
    val textColor = when {
        isCorrect       -> if (isDark) Color(0xFFA8FFD1) else Color(0xFF166534)
        isWrongSelected -> if (isDark) Color(0xFFFFB3B3) else Color(0xFFB91C1C)
        isSelected      -> if (isDark) Color(0xFF93C5FD) else Color(0xFF1D4ED8)
        else            -> if (isDark) Color.White       else Color(0xFF0F172A)
    }
    val labelBg = when {
        isCorrect || isWrongSelected || isSelected ->
            if (isCorrect) Color(0xFF27D17F) else if (isWrongSelected) Color(0xFFFF6B6B) else Color(0xFF16C6D9)
        else -> if (isDark) Color.White.copy(alpha = 0.10f) else Color(0xFFF1F5F9)
    }
    val labelTextColor = when {
        isCorrect || isWrongSelected || isSelected -> Color.White
        else -> if (isDark) Color(0xFF8FA3C8) else Color(0xFF64748B)
    }

    OutlinedButton(
        onClick        = onClick,
        enabled        = enabled,
        modifier       = Modifier.fillMaxWidth(),
        shape          = RoundedCornerShape(16.dp),
        border         = BorderStroke(1.5.dp, borderColor),
        colors         = ButtonDefaults.outlinedButtonColors(
            containerColor         = backgroundColor,
            contentColor           = textColor,
            disabledContainerColor = backgroundColor,
            disabledContentColor   = textColor
        ),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier         = Modifier.size(36.dp).clip(CircleShape).background(labelBg),
                contentAlignment = Alignment.Center
            ) {
                Text(optionLabel, color = labelTextColor, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text       = optionText,
                modifier   = Modifier.weight(1f),
                color      = textColor,
                fontSize   = 15.sp,
                lineHeight = 22.sp,
                fontWeight = if (isCorrect || isWrongSelected || isSelected) FontWeight.SemiBold else FontWeight.Normal,
                textAlign  = TextAlign.Start
            )
            if (isCorrect) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF27D17F), modifier = Modifier.size(18.dp))
            } else if (isWrongSelected) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.Cancel, null, tint = Color(0xFFFF6B6B), modifier = Modifier.size(18.dp))
            }
        }
    }
}

private val previewQuestions = listOf(
    QuizQuestionUi(1, "Which language is preferred for Android?", listOf("Java", "Swift", "Kotlin", "Python"), "Kotlin"),
    QuizQuestionUi(2, "What does CPU stand for?", listOf("Central Processing Unit", "Central Program Utility", "Control Processing Unit", "Computer Power Utility"), "Central Processing Unit")
)

@Preview(name = "Quiz Light", showBackground = true, backgroundColor = 0xFFF3F7FC, uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true)
@Composable
private fun QuizScreenLightPreview() {
    StudentApplicationTheme(darkTheme = false) { QuizScreen(questions = previewQuestions, topicTitle = "Technology Quiz") }
}

@Preview(name = "Quiz Dark", showBackground = true, backgroundColor = 0xFF000B1B, uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun QuizScreenDarkPreview() {
    StudentApplicationTheme(darkTheme = true) { QuizScreen(questions = previewQuestions, topicTitle = "Technology Quiz") }
}