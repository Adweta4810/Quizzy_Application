package com.dma.studentapplication.ui.screens.quiz

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
import com.dma.studentapplication.ui.screens.review.ReviewQuestionItem
import com.dma.studentapplication.ui.theme.StudentApplicationTheme
import com.dma.studentapplication.viewmodel.QuizViewModel
import kotlinx.coroutines.delay

// ── Public entry point ────────────────────────────────────────────────────────

/**
 * ViewModel-connected entry point for the quiz screen.
 *
 * Collects the question list from [viewModel] and delegates all UI to
 * [QuizScreenContent], keeping the ViewModel dependency out of the stateless
 * composable so it can be previewed and tested independently.
 *
 * @param viewModel      Provides the shuffled question list for this quiz session.
 * @param topicTitle     Display name shown in the top bar (e.g. "Technology Quiz").
 * @param onBackClick    Called when the user chooses to quit and discard the session.
 * @param onQuizFinished Called with (score, total, reviewItems) when the quiz ends.
 */
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

// ── Stateless content ─────────────────────────────────────────────────────────

/**
 * Stateless quiz screen that drives the full question/answer/timer flow.
 *
 * All state is held with [rememberSaveable] so it survives configuration changes
 * (rotation, system font scale, etc.) without needing the ViewModel.
 *
 * Flow per question:
 * 1. Timer counts down from 15 s — auto-locks with "No answer" if it reaches 0.
 * 2. User taps an option → selection is highlighted but not yet locked.
 * 3. User taps "Lock Answer" → answer is evaluated, RoboBuddy reacts, correct
 *    answer is revealed if wrong.
 * 4. User taps "Next Question" → state resets for the next question.
 * 5. After the last question, "Finish Quiz" calls [onQuizFinished].
 *
 * @param questions      Shuffled list of questions for this session.
 * @param topicTitle     Display name shown in the top bar.
 * @param onBackClick    Called when the user quits without finishing.
 * @param onQuizFinished Called with (score, total, reviewItems) when the quiz ends.
 */
@Composable
internal fun QuizScreenContent(
    questions: List<QuizQuestionUi>,
    topicTitle: String = "Quizzy Quiz",
    onBackClick: () -> Unit = {},
    onQuizFinished: (Int, Int, List<ReviewQuestionItem>) -> Unit = { _, _, _ -> }
) {
    val isDark = isSystemInDarkTheme()
    val totalQuestions = questions.size

    // ── Per-question state (all saveable across config changes) ───────────────

    var currentQuestionIndex by rememberSaveable { mutableIntStateOf(0) }
    var selectedAnswerIndex by rememberSaveable { mutableStateOf<Int?>(null) }
    var answerLocked by rememberSaveable { mutableStateOf(false) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var timeLeft by rememberSaveable { mutableIntStateOf(15) }
    var showQuitDialog by rememberSaveable { mutableStateOf(false) }

    // Review items accumulate as the user answers each question.
    // A custom listSaver is used because MutableStateList isn't Saveable by default.
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

    // Intercept the system back gesture to show the quit dialog instead of navigating away
    BackHandler { showQuitDialog = true }

    // ── Loading guard ─────────────────────────────────────────────────────────
    if (questions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading quiz...")
        }
        return
    }

    // ── Quiz completion guard ─────────────────────────────────────────────────
    // currentQuestion becomes null when the index goes past the last question
    val currentQuestion = questions.getOrNull(currentQuestionIndex)

    if (currentQuestion == null) {
        LaunchedEffect(Unit) {
            onQuizFinished(score, totalQuestions, reviewItems.toList())
        }
        return
    }

    // Index of the correct option in the (already shuffled) options list
    val correctIndex = currentQuestion.options.indexOf(currentQuestion.correctAnswer)

    // ── Theme-aware colors ────────────────────────────────────────────────────
    val screenBg       = if (isDark) Color(0xFF001226) else Color(0xFFF5F8FC)
    val screenBgBottom = if (isDark) Color(0xFF041B38) else Color(0xFFEAF2FB)
    val surfaceColor   = if (isDark) Color(0xFF0B1F3A) else Color.White
    val cardBorder     = if (isDark) Color(0xFF37506F) else Color(0xFFD7E0EA)
    val titleColor     = if (isDark) Color.White       else Color(0xFF0F172A)
    val subtitleColor  = if (isDark) Color(0xFFD6E0F5) else Color(0xFF475569)

    val accentGreen  = Color(0xFF22C55E)
    val darkGreen    = Color(0xFF14532D)
    val accentTeal   = Color(0xFF0891B2)
    val warningColor = Color(0xFFE58A00)
    val wrongColor   = Color(0xFFDC2626)

    // Smooth progress bar animation when moving to the next question
    val animatedProgress by animateFloatAsState(
        targetValue    = (currentQuestionIndex + 1) / totalQuestions.toFloat(),
        animationSpec  = tween(500),
        label          = "progress"
    )

    // RoboBuddy reacts to the current answer state
    val roboState = when {
        answerLocked && selectedAnswerIndex == correctIndex -> RoboBuddyState.CORRECT
        answerLocked && selectedAnswerIndex != null         -> RoboBuddyState.INCORRECT
        answerLocked                                        -> RoboBuddyState.SAD       // Timed out
        timeLeft <= 5                                       -> RoboBuddyState.SURPRISED // Low time
        else                                                -> RoboBuddyState.THINKING
    }

    // ── Quit dialog ───────────────────────────────────────────────────────────
    if (showQuitDialog) {
        QuitQuizDialog(
            onDismiss = { /* Dialog stays open — user must choose an option */ },
            onQuit    = { onBackClick() },
            onEnd     = { onQuizFinished(score, totalQuestions, reviewItems.toList()) }
        )
    }

    // ── Countdown timer ───────────────────────────────────────────────────────
    // Restarts whenever the question index changes or the answer is locked.
    // If time reaches 0 before the user locks an answer, auto-lock with no selection.
    LaunchedEffect(currentQuestionIndex, answerLocked) {
        while (timeLeft > 0 && !answerLocked) {
            delay(1000)
            timeLeft--
        }

        if (timeLeft == 0 && !answerLocked) {
            reviewItems.add(
                ReviewQuestionItem(
                    question       = currentQuestion.question,
                    selectedAnswer = "No answer selected",
                    correctAnswer  = currentQuestion.correctAnswer,
                    isCorrect      = false
                )
            )
            answerLocked = true
        }
    }

    // ── UI ────────────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(screenBg, screenBgBottom)))
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        LazyColumn(
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // ── Top bar: back button, topic title, question counter, score chip ──
            item {
                Row(
                    modifier          = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back button opens the quit dialog rather than navigating immediately
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape    = CircleShape,
                        color    = surfaceColor,
                        border   = BorderStroke(1.dp, cardBorder)
                    ) {
                        IconButton(onClick = { showQuitDialog = true }) {
                            Icon(
                                imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint               = titleColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text       = topicTitle,
                            color      = titleColor,
                            fontSize   = 21.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines   = 1,
                            overflow   = TextOverflow.Ellipsis
                        )

                        Text(
                            text       = "Question ${currentQuestionIndex + 1} of $totalQuestions",
                            color      = subtitleColor,
                            fontSize   = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    ScoreChip(score = score, isDark = isDark)
                }
            }

            // ── Progress bar + timer chip ─────────────────────────────────────
            item {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    LinearProgressIndicator(
                        progress  = { animatedProgress },
                        modifier  = Modifier.weight(1f).height(9.dp).clip(RoundedCornerShape(50.dp)),
                        color      = accentGreen,
                        trackColor = if (isDark) Color(0xFF28415F) else Color(0xFFDDE7F0),
                        strokeCap  = StrokeCap.Round
                    )

                    TimerChip(
                        timeLeft     = timeLeft,
                        accentGreen  = accentGreen,
                        warningColor = warningColor,
                        wrongColor   = wrongColor
                    )
                }
            }

            // ── RoboBuddy feedback card ───────────────────────────────────────
            // Shows a dynamic message and mascot that reacts to the current state
            item {
                Card(
                    modifier  = Modifier.fillMaxWidth(),
                    shape     = RoundedCornerShape(26.dp),
                    colors    = CardDefaults.cardColors(containerColor = Color.Transparent),
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
                            state    = roboState,
                            modifier = Modifier.size(78.dp)
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            // Headline changes based on answer state
                            Text(
                                text = when {
                                    answerLocked && selectedAnswerIndex == correctIndex -> "Nice one!"
                                    answerLocked && selectedAnswerIndex != null         -> "Oops, not this time"
                                    answerLocked                                        -> "Time is up!"
                                    timeLeft <= 5                                       -> "Hurry up!"
                                    else                                                -> "Think carefully!"
                                },
                                color      = Color.White,
                                fontSize   = 19.sp,
                                fontWeight = FontWeight.ExtraBold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Sub-message gives more context beneath the headline
                            Text(
                                text = when {
                                    answerLocked && selectedAnswerIndex == correctIndex -> "Robo is proud of you!"
                                    answerLocked && selectedAnswerIndex != null         -> "Check the correct answer below."
                                    answerLocked                                        -> "You ran out of time."
                                    else -> "You have ${timeLeft}s for this question."
                                },
                                color      = Color.White,
                                fontSize   = 15.sp,
                                lineHeight = 21.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // ── Question card ─────────────────────────────────────────────────
            item {
                Card(
                    modifier  = Modifier.fillMaxWidth(),
                    shape     = RoundedCornerShape(24.dp),
                    colors    = CardDefaults.cardColors(containerColor = surfaceColor),
                    border    = BorderStroke(1.dp, cardBorder),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text       = "Question ${currentQuestionIndex + 1}",
                            color      = accentTeal,
                            fontSize   = 13.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text       = currentQuestion.question,
                            color      = titleColor,
                            fontSize   = 21.sp,
                            lineHeight = 29.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // ── Answer options ────────────────────────────────────────────────
            // Each option is keyed by question index + option index to force recomposition
            // when moving to a new question, preventing stale selected/correct states.
            items(
                items = currentQuestion.options.mapIndexed { index, option -> index to option },
                key   = { (index, _) -> "option_${currentQuestionIndex}_$index" }
            ) { (index, option) ->
                QuizOptionItem(
                    optionLabel      = ('A' + index).toString(),
                    optionText       = option,
                    isSelected       = selectedAnswerIndex == index,
                    isCorrect        = answerLocked && index == correctIndex,
                    isWrongSelected  = answerLocked && selectedAnswerIndex == index && index != correctIndex,
                    enabled          = !answerLocked,
                    isDark           = isDark,
                    onClick          = {
                        // Only allow selection before locking
                        if (!answerLocked) selectedAnswerIndex = index
                    }
                )
            }

            // ── Correct answer reveal ─────────────────────────────────────────
            // Only shown after locking when the user got it wrong or timed out
            if (answerLocked && selectedAnswerIndex != correctIndex) {
                item {
                    CorrectAnswerReveal(
                        correctAnswer = currentQuestion.correctAnswer,
                        isDark        = isDark
                    )
                }
            }

            // ── Primary action button ─────────────────────────────────────────
            // Label and behavior change depending on the current answer state:
            // "Lock Answer" → evaluates selection and locks the question
            // "Next Question" → advances to the next question and resets state
            // "Finish Quiz"   → triggers the quiz completion callback
            item {
                Box(
                    modifier         = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            if (!answerLocked) {
                                // ── Lock the answer ───────────────────────────
                                val isCorrect = selectedAnswerIndex == correctIndex

                                if (isCorrect) score++

                                reviewItems.add(
                                    ReviewQuestionItem(
                                        question       = currentQuestion.question,
                                        selectedAnswer = selectedAnswerIndex
                                            ?.let { currentQuestion.options[it] }
                                            ?: "No answer selected",
                                        correctAnswer  = currentQuestion.correctAnswer,
                                        isCorrect      = isCorrect
                                    )
                                )

                                answerLocked = true
                            } else {
                                // ── Advance or finish ─────────────────────────
                                if (currentQuestionIndex < totalQuestions - 1) {
                                    // Move to the next question and reset all per-question state
                                    currentQuestionIndex++
                                    selectedAnswerIndex = null
                                    answerLocked        = false
                                    timeLeft            = 15
                                } else {
                                    onQuizFinished(score, totalQuestions, reviewItems.toList())
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 520.dp)
                            .height(56.dp),
                        shape  = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor         = darkGreen,
                            contentColor           = Color.White,
                            disabledContainerColor = Color(0xFF334155),
                            disabledContentColor   = Color(0xFFE2E8F0)
                        ),
                        // Disabled until the user selects an option (or answer is already locked)
                        enabled = selectedAnswerIndex != null || answerLocked
                    ) {
                        Text(
                            text = when {
                                !answerLocked                              -> "Lock Answer"
                                currentQuestionIndex == totalQuestions - 1 -> "Finish Quiz"
                                else                                       -> "Next Question"
                            },
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}

// ── Dialogs ───────────────────────────────────────────────────────────────────

/**
 * Confirmation dialog shown when the user tries to leave mid-quiz.
 *
 * Offers three choices:
 * - **Keep Going** — dismiss and resume the quiz ([onDismiss])
 * - **Quit**       — exit without saving any result ([onQuit])
 * - **End Session** — finish early and navigate to the result screen ([onEnd])
 */
@Composable
private fun QuitQuizDialog(
    onDismiss: () -> Unit,
    onQuit: () -> Unit,
    onEnd: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Quit the quiz?", fontWeight = FontWeight.ExtraBold)
        },
        text = {
            Text("Do you want to keep going, quit the quiz, or end this session and view your result?")
        },
        confirmButton = {
            Button(onClick = onEnd) { Text("End Session") }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onDismiss) { Text("Keep Going") }
                TextButton(onClick = onQuit)    { Text("Quit") }
            }
        }
    )
}

// ── Chips ─────────────────────────────────────────────────────────────────────

/**
 * Small pill showing the user's running correct-answer count during the quiz.
 *
 * @param score  Number of correct answers so far.
 * @param isDark Whether dark mode is active (affects background and text colors).
 */
@Composable
private fun ScoreChip(score: Int, isDark: Boolean) {
    Surface(
        shape  = RoundedCornerShape(14.dp),
        color  = if (isDark) Color(0xFF064E3B) else Color(0xFFDCFCE7),
        border = BorderStroke(1.dp, if (isDark) Color(0xFF34D399) else Color(0xFF16A34A))
    ) {
        Text(
            text     = "Score $score",
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            color    = if (isDark) Color.White else Color(0xFF14532D),
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

/**
 * Countdown timer pill that smoothly changes color as time runs low.
 *
 * Color thresholds:
 * - > 5 s  → green  (plenty of time)
 * - ≤ 5 s  → amber  (warning)
 * - ≤ 3 s  → red    (critical)
 *
 * @param timeLeft     Seconds remaining for the current question.
 * @param accentGreen  Color used when time is plentiful.
 * @param warningColor Color used when time is running low.
 * @param wrongColor   Color used when time is critically low.
 */
@Composable
private fun TimerChip(
    timeLeft: Int,
    accentGreen: Color,
    warningColor: Color,
    wrongColor: Color
) {
    val timerColor by animateColorAsState(
        targetValue   = when {
            timeLeft <= 3 -> wrongColor
            timeLeft <= 5 -> warningColor
            else          -> accentGreen
        },
        animationSpec = tween(300),
        label         = "timerColor"
    )

    Surface(
        shape  = RoundedCornerShape(14.dp),
        color  = Color.Transparent,
        border = BorderStroke(1.dp, timerColor)
    ) {
        Row(
            modifier          = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector        = Icons.Default.Timer,
                contentDescription = "Timer",
                tint               = timerColor,
                modifier           = Modifier.size(17.dp)
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text       = "${timeLeft}s",
                color      = timerColor,
                fontWeight = FontWeight.ExtraBold,
                fontSize   = 15.sp
            )
        }
    }
}

// ── Answer options ────────────────────────────────────────────────────────────

/**
 * Single answer option button with animated color feedback.
 *
 * States (mutually exclusive, evaluated in priority order):
 * - [isCorrect]       — green background/border, checkmark icon
 * - [isWrongSelected] — red background/border, X icon
 * - [isSelected]      — teal highlight (pre-lock selection)
 * - default           — neutral white/dark surface
 *
 * Disabled ([enabled] = false) once the answer is locked so the user cannot change it,
 * but the colors are preserved via [disabledContainerColor] / [disabledContentColor].
 *
 * @param optionLabel     Letter label ("A", "B", "C", "D") shown in the leading circle.
 * @param optionText      The answer text.
 * @param isSelected      Whether this option is currently selected (pre-lock).
 * @param isCorrect       Whether this is the correct option and the answer is locked.
 * @param isWrongSelected Whether the user selected this option, but it is wrong.
 * @param enabled         False once the answer is locked.
 * @param isDark          Whether dark mode is active.
 * @param onClick         Called when the option is tapped.
 */
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
    // Background and border animate smoothly when states change after locking
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isCorrect       -> if (isDark) Color(0xFF064E3B) else Color(0xFFDCFCE7)
            isWrongSelected -> if (isDark) Color(0xFF7F1D1D) else Color(0xFFFEE2E2)
            isSelected      -> if (isDark) Color(0xFF164E63) else Color(0xFFE0F2FE)
            else            -> if (isDark) Color(0xFF0B1F3A) else Color.White
        },
        animationSpec = tween(200),
        label         = "optionBg"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isCorrect       -> Color(0xFF22C55E)
            isWrongSelected -> Color(0xFFDC2626)
            isSelected      -> Color(0xFF0891B2)
            else            -> if (isDark) Color(0xFF37506F) else Color(0xFFD7E0EA)
        },
        animationSpec = tween(200),
        label         = "optionBorder"
    )

    // Text color doesn't animate — changes immediately with state
    val textColor = when {
        isDark          -> Color.White
        isCorrect       -> Color(0xFF14532D)
        isWrongSelected -> Color(0xFF7F1D1D)
        isSelected      -> Color(0xFF0C4A6E)
        else            -> Color(0xFF0F172A)
    }

    OutlinedButton(
        onClick  = onClick,
        enabled  = enabled,
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(18.dp),
        border   = BorderStroke(1.5.dp, borderColor),
        colors   = ButtonDefaults.outlinedButtonColors(
            containerColor         = backgroundColor,
            contentColor           = textColor,
            // Preserve colors when disabled so the feedback remains visible after locking
            disabledContainerColor = backgroundColor,
            disabledContentColor   = textColor
        ),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp)
    ) {
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Letter badge circle — color matches the border for visual consistency
            Surface(
                modifier = Modifier.size(36.dp),
                shape    = CircleShape,
                color    = borderColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text       = optionLabel,
                        color      = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize   = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text       = optionText,
                modifier   = Modifier.weight(1f),
                color      = textColor,
                fontSize   = 15.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign  = TextAlign.Start
            )

            // Trailing icon only appears after the answer is locked
            if (isCorrect) {
                Icon(
                    imageVector        = Icons.Default.CheckCircle,
                    contentDescription = "Correct answer",
                    tint               = Color(0xFF22C55E),
                    modifier           = Modifier.size(20.dp)
                )
            }

            if (isWrongSelected) {
                Icon(
                    imageVector        = Icons.Default.Cancel,
                    contentDescription = "Wrong answer",
                    tint               = Color(0xFFDC2626),
                    modifier           = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ── Correct answer reveal ─────────────────────────────────────────────────────

/**
 * Green banner shown below the options when the user answered incorrectly or timed out.
 * Explicitly displays the correct answer text so the user can learn from the mistake.
 *
 * @param correctAnswer The text of the correct answer option.
 * @param isDark        Whether dark mode is active.
 */
@Composable
private fun CorrectAnswerReveal(correctAnswer: String, isDark: Boolean) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(18.dp),
        colors    = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF064E3B) else Color(0xFFDCFCE7)
        ),
        border    = BorderStroke(1.dp, if (isDark) Color(0xFF34D399) else Color(0xFF16A34A)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier          = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector        = Icons.Default.CheckCircle,
                contentDescription = null,
                tint               = if (isDark) Color.White else Color(0xFF14532D),
                modifier           = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text       = "Correct Answer",
                    color      = if (isDark) Color.White else Color(0xFF14532D),
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text       = correctAnswer,
                    color      = if (isDark) Color.White else Color(0xFF14532D),
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

// ── Preview data & previews ───────────────────────────────────────────────────

private val previewQuestions = listOf(
    QuizQuestionUi(
        id            = 1,
        question      = "Which language is preferred for Android development?",
        options       = listOf("Java", "Swift", "Kotlin", "Python"),
        correctAnswer = "Kotlin"
    ),
    QuizQuestionUi(
        id            = 2,
        question      = "What does CPU stand for?",
        options       = listOf(
            "Central Processing Unit",
            "Central Program Utility",
            "Control Processing Unit",
            "Computer Power Utility"
        ),
        correctAnswer = "Central Processing Unit"
    )
)

@Preview(name = "Quiz Light", showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun QuizScreenLightPreview() {
    StudentApplicationTheme(darkTheme = false) {
        QuizScreenContent(questions = previewQuestions, topicTitle = "Technology Quiz")
    }
}

@Preview(
    name        = "Quiz Dark Landscape",
    showBackground = true,
    showSystemUi   = true,
    widthDp        = 900,
    heightDp       = 420,
    uiMode         = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun QuizScreenDarkLandscapePreview() {
    StudentApplicationTheme(darkTheme = true) {
        QuizScreenContent(questions = previewQuestions, topicTitle = "Technology Quiz")
    }
}