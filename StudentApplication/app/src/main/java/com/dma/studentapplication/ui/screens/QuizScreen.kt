package com.dma.studentapplication.ui.screens

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dma.studentapplication.model.RoboBuddyState
import com.dma.studentapplication.ui.components.RoboBuddy
import com.dma.studentapplication.ui.screens.model.QuizQuestionUi
import com.dma.studentapplication.ui.screens.model.ReviewQuestionItem
import com.dma.studentapplication.ui.theme.StudentApplicationTheme
import com.dma.studentapplication.viewmodel.QuizViewModel
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    topicTitle: String = "Quizzy Quiz",
    onBackClick: () -> Unit = {},
    onQuizFinished: (Int, Int, List<ReviewQuestionItem>) -> Unit = { _, _, _ -> }
) {
    val questions by viewModel.questions.collectAsStateWithLifecycle()

    QuizScreenContent(
        questions = questions,
        topicTitle = topicTitle,
        onBackClick = onBackClick,
        onQuizFinished = onQuizFinished
    )
}

@Composable
internal fun QuizScreenContent(
    questions: List<QuizQuestionUi>,
    topicTitle: String = "Quizzy Quiz",
    onBackClick: () -> Unit = {},
    onQuizFinished: (Int, Int, List<ReviewQuestionItem>) -> Unit = { _, _, _ -> }
) {
    val isDark = isSystemInDarkTheme()
    val totalQuestions = questions.size

    var currentQuestionIndex by rememberSaveable { mutableIntStateOf(0) }
    var selectedAnswerIndex by rememberSaveable { mutableStateOf<Int?>(null) }
    var answerLocked by rememberSaveable { mutableStateOf(false) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var timeLeft by rememberSaveable { mutableIntStateOf(15) }
    var showQuitDialog by rememberSaveable { mutableStateOf(false) }

    val reviewItems = rememberSaveable(
        saver = listSaver(
            save = { list ->
                list.map {
                    listOf(
                        it.question,
                        it.selectedAnswer,
                        it.correctAnswer,
                        it.isCorrect.toString()
                    )
                }
            },
            restore = { saved ->
                saved.map { raw ->
                    ReviewQuestionItem(
                        question = raw[0],
                        selectedAnswer = raw[1],
                        correctAnswer = raw[2],
                        isCorrect = raw[3].toBoolean()
                    )
                }.toMutableStateList()
            }
        )
    ) {
        mutableStateListOf<ReviewQuestionItem>()
    }

    BackHandler { showQuitDialog = true }

    if (questions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading quiz...")
        }
        return
    }

    val currentQuestion = questions.getOrNull(currentQuestionIndex)

    if (currentQuestion == null) {
        LaunchedEffect(Unit) {
            onQuizFinished(score, totalQuestions, reviewItems.toList())
        }
        return
    }

    val correctIndex = currentQuestion.options.indexOf(currentQuestion.correctAnswer)

    val screenBg = if (isDark) Color(0xFF001226) else Color(0xFFF5F8FC)
    val screenBgBottom = if (isDark) Color(0xFF041B38) else Color(0xFFEAF2FB)
    val surfaceColor = if (isDark) Color(0xFF0B1F3A) else Color.White
    val cardBorder = if (isDark) Color(0xFF37506F) else Color(0xFFD7E0EA)
    val titleColor = if (isDark) Color.White else Color(0xFF0F172A)
    val subtitleColor = if (isDark) Color(0xFFD6E0F5) else Color(0xFF475569)

    val accentGreen = Color(0xFF22C55E)
    val darkGreen = Color(0xFF14532D)
    val accentTeal = Color(0xFF0891B2)
    val warningColor = Color(0xFFE58A00)
    val wrongColor = Color(0xFFDC2626)

    val animatedProgress by animateFloatAsState(
        targetValue = (currentQuestionIndex + 1) / totalQuestions.toFloat(),
        animationSpec = tween(500),
        label = "progress"
    )

    val roboState = when {
        answerLocked && selectedAnswerIndex == correctIndex -> RoboBuddyState.CORRECT
        answerLocked && selectedAnswerIndex != null -> RoboBuddyState.INCORRECT
        answerLocked -> RoboBuddyState.SAD
        timeLeft <= 5 -> RoboBuddyState.SURPRISED
        else -> RoboBuddyState.THINKING
    }

    if (showQuitDialog) {
        QuitQuizDialog(
            onDismiss = { },
            onQuit = {
                onBackClick()
            },
            onEnd = {
                onQuizFinished(score, totalQuestions, reviewItems.toList())
            }
        )
    }

    LaunchedEffect(currentQuestionIndex, answerLocked) {
        while (timeLeft > 0 && !answerLocked) {
            delay(1000)
            timeLeft--
        }

        if (timeLeft == 0 && !answerLocked) {
            reviewItems.add(
                ReviewQuestionItem(
                    question = currentQuestion.question,
                    selectedAnswer = "No answer selected",
                    correctAnswer = currentQuestion.correctAnswer,
                    isCorrect = false
                )
            )
            answerLocked = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(screenBg, screenBgBottom)))
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 18.dp,
                bottom = 28.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        color = surfaceColor,
                        border = BorderStroke(1.dp, cardBorder)
                    ) {
                        IconButton(onClick = { showQuitDialog = true }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = titleColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = topicTitle,
                            color = titleColor,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "Question ${currentQuestionIndex + 1} of $totalQuestions",
                            color = subtitleColor,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    ScoreChip(score = score, isDark = isDark)
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .weight(1f)
                            .height(9.dp)
                            .clip(RoundedCornerShape(50.dp)),
                        color = accentGreen,
                        trackColor = if (isDark) Color(0xFF28415F) else Color(0xFFDDE7F0),
                        strokeCap = StrokeCap.Round
                    )

                    TimerChip(
                        timeLeft = timeLeft,
                        accentGreen = accentGreen,
                        warningColor = warningColor,
                        wrongColor = wrongColor
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF0F766E), Color(0xFF16A34A))
                                )
                            )
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RoboBuddy(
                            state = roboState,
                            modifier = Modifier.size(78.dp)
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = when {
                                    answerLocked && selectedAnswerIndex == correctIndex -> "Nice one!"
                                    answerLocked && selectedAnswerIndex != null -> "Oops, not this time"
                                    answerLocked -> "Time is up!"
                                    timeLeft <= 5 -> "Hurry up!"
                                    else -> "Think carefully!"
                                },
                                color = Color.White,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.ExtraBold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = when {
                                    answerLocked && selectedAnswerIndex == correctIndex -> "Robo is proud of you!"
                                    answerLocked && selectedAnswerIndex != null -> "Check the correct answer below."
                                    answerLocked -> "You ran out of time."
                                    else -> "You have ${timeLeft}s for this question."
                                },
                                color = Color.White,
                                fontSize = 15.sp,
                                lineHeight = 21.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = surfaceColor),
                    border = BorderStroke(1.dp, cardBorder),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Question ${currentQuestionIndex + 1}",
                            color = accentTeal,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = currentQuestion.question,
                            color = titleColor,
                            fontSize = 21.sp,
                            lineHeight = 29.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            items(
                items = currentQuestion.options.mapIndexed { index, option -> index to option },
                key = { (index, _) -> "option_${currentQuestionIndex}_$index" }
            ) { (index, option) ->
                QuizOptionItem(
                    optionLabel = ('A' + index).toString(),
                    optionText = option,
                    isSelected = selectedAnswerIndex == index,
                    isCorrect = answerLocked && index == correctIndex,
                    isWrongSelected = answerLocked && selectedAnswerIndex == index && index != correctIndex,
                    enabled = !answerLocked,
                    isDark = isDark,
                    onClick = {
                        if (!answerLocked) selectedAnswerIndex = index
                    }
                )
            }

            if (answerLocked && selectedAnswerIndex != correctIndex) {
                item {
                    CorrectAnswerReveal(
                        correctAnswer = currentQuestion.correctAnswer,
                        isDark = isDark
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            if (!answerLocked) {
                                val isCorrect = selectedAnswerIndex == correctIndex

                                if (isCorrect) score++

                                reviewItems.add(
                                    ReviewQuestionItem(
                                        question = currentQuestion.question,
                                        selectedAnswer = selectedAnswerIndex
                                            ?.let { currentQuestion.options[it] }
                                            ?: "No answer selected",
                                        correctAnswer = currentQuestion.correctAnswer,
                                        isCorrect = isCorrect
                                    )
                                )

                                answerLocked = true
                            } else {
                                if (currentQuestionIndex < totalQuestions - 1) {
                                    currentQuestionIndex++
                                    selectedAnswerIndex = null
                                    answerLocked = false
                                    timeLeft = 15
                                } else {
                                    onQuizFinished(score, totalQuestions, reviewItems.toList())
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 520.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = darkGreen,
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFF334155),
                            disabledContentColor = Color(0xFFE2E8F0)
                        ),
                        enabled = selectedAnswerIndex != null || answerLocked
                    ) {
                        Text(
                            text = when {
                                !answerLocked -> "Lock Answer"
                                currentQuestionIndex == totalQuestions - 1 -> "Finish Quiz"
                                else -> "Next Question"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuitQuizDialog(
    onDismiss: () -> Unit,
    onQuit: () -> Unit,
    onEnd: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Quit the quiz?",
                fontWeight = FontWeight.ExtraBold
            )
        },
        text = {
            Text("Do you want to keep going, quit the quiz, or end this session and view your result?")
        },
        confirmButton = {
            Button(onClick = onEnd) {
                Text("End Session")
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onDismiss) {
                    Text("Keep Going")
                }

                TextButton(onClick = onQuit) {
                    Text("Quit")
                }
            }
        }
    )
}

@Composable
private fun ScoreChip(
    score: Int,
    isDark: Boolean
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isDark) Color(0xFF064E3B) else Color(0xFFDCFCE7),
        border = BorderStroke(
            1.dp,
            if (isDark) Color(0xFF34D399) else Color(0xFF16A34A)
        )
    ) {
        Text(
            text = "Score $score",
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            color = if (isDark) Color.White else Color(0xFF14532D),
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun TimerChip(
    timeLeft: Int,
    accentGreen: Color,
    warningColor: Color,
    wrongColor: Color
) {
    val timerColor by animateColorAsState(
        targetValue = when {
            timeLeft <= 3 -> wrongColor
            timeLeft <= 5 -> warningColor
            else -> accentGreen
        },
        animationSpec = tween(300),
        label = "timerColor"
    )

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, timerColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = "Timer",
                tint = timerColor,
                modifier = Modifier.size(17.dp)
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "${timeLeft}s",
                color = timerColor,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp
            )
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
        targetValue = when {
            isCorrect -> if (isDark) Color(0xFF064E3B) else Color(0xFFDCFCE7)
            isWrongSelected -> if (isDark) Color(0xFF7F1D1D) else Color(0xFFFEE2E2)
            isSelected -> if (isDark) Color(0xFF164E63) else Color(0xFFE0F2FE)
            else -> if (isDark) Color(0xFF0B1F3A) else Color.White
        },
        animationSpec = tween(200),
        label = "optionBg"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isCorrect -> Color(0xFF22C55E)
            isWrongSelected -> Color(0xFFDC2626)
            isSelected -> Color(0xFF0891B2)
            else -> if (isDark) Color(0xFF37506F) else Color(0xFFD7E0EA)
        },
        animationSpec = tween(200),
        label = "optionBorder"
    )

    val textColor = when {
        isDark -> Color.White
        isCorrect -> Color(0xFF14532D)
        isWrongSelected -> Color(0xFF7F1D1D)
        isSelected -> Color(0xFF0C4A6E)
        else -> Color(0xFF0F172A)
    }

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.5.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = backgroundColor,
            disabledContentColor = textColor
        ),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                color = borderColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = optionLabel,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = optionText,
                modifier = Modifier.weight(1f),
                color = textColor,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start
            )

            if (isCorrect) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Correct answer",
                    tint = Color(0xFF22C55E),
                    modifier = Modifier.size(20.dp)
                )
            }

            if (isWrongSelected) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = "Wrong answer",
                    tint = Color(0xFFDC2626),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun CorrectAnswerReveal(
    correctAnswer: String,
    isDark: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF064E3B) else Color(0xFFDCFCE7)
        ),
        border = BorderStroke(
            1.dp,
            if (isDark) Color(0xFF34D399) else Color(0xFF16A34A)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (isDark) Color.White else Color(0xFF14532D),
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = "Correct Answer",
                    color = if (isDark) Color.White else Color(0xFF14532D),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = correctAnswer,
                    color = if (isDark) Color.White else Color(0xFF14532D),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

private val previewQuestions = listOf(
    QuizQuestionUi(
        id = 1,
        question = "Which language is preferred for Android development?",
        options = listOf("Java", "Swift", "Kotlin", "Python"),
        correctAnswer = "Kotlin"
    ),
    QuizQuestionUi(
        id = 2,
        question = "What does CPU stand for?",
        options = listOf(
            "Central Processing Unit",
            "Central Program Utility",
            "Control Processing Unit",
            "Computer Power Utility"
        ),
        correctAnswer = "Central Processing Unit"
    )
)

@Preview(
    name = "Quiz Light",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun QuizScreenLightPreview() {
    StudentApplicationTheme(darkTheme = false) {
        QuizScreenContent(
            questions = previewQuestions,
            topicTitle = "Technology Quiz"
        )
    }
}

@Preview(
    name = "Quiz Dark Landscape",
    showBackground = true,
    showSystemUi = true,
    widthDp = 900,
    heightDp = 420,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun QuizScreenDarkLandscapePreview() {
    StudentApplicationTheme(darkTheme = true) {
        QuizScreenContent(
            questions = previewQuestions,
            topicTitle = "Technology Quiz"
        )
    }
}