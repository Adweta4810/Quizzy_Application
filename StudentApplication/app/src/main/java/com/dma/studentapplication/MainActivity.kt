package com.dma.studentapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dma.studentapplication.data.local.room.QuizDatabase
import com.dma.studentapplication.navigation.Screen
import com.dma.studentapplication.repository.QuizRepository
import com.dma.studentapplication.ui.QuizViewModel
import com.dma.studentapplication.ui.QuizViewModelFactory
import com.dma.studentapplication.ui.screens.HistoryDetailScreen
import com.dma.studentapplication.ui.screens.HistoryScreen
import com.dma.studentapplication.ui.screens.HomeScreen
import com.dma.studentapplication.ui.screens.ProfileScreen
import com.dma.studentapplication.ui.screens.QuizHistoryDetail
import com.dma.studentapplication.ui.screens.QuizHistoryItem
import com.dma.studentapplication.ui.screens.QuizScreen
import com.dma.studentapplication.ui.screens.ResultScreen
import com.dma.studentapplication.ui.screens.ReviewQuestionItem
import com.dma.studentapplication.ui.screens.ReviewScreen
import com.dma.studentapplication.ui.screens.SplashScreen
import com.dma.studentapplication.ui.screens.TopicScreen
import com.dma.studentapplication.ui.theme.StudentApplicationTheme
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ── Dependency graph (manual DI) ──────────────────────────────────────
        val db         = QuizDatabase.getInstance(this)
        val repository = QuizRepository(db.quizResultDao(), this)
        val vmFactory  = QuizViewModelFactory(repository)

        setContent {
            StudentApplicationTheme {
                val navController = rememberNavController()
                val viewModel: QuizViewModel = viewModel(factory = vmFactory)

                // Collect all ROOM results once at the top level so every
                // screen that needs them (History, Profile) uses the same Flow.
                val allResults by viewModel.allResults.collectAsStateWithLifecycle()

                NavHost(
                    navController    = navController,
                    startDestination = Screen.Splash.route
                ) {

                    // ─────────────────────────────────────────────────────────
                    // SPLASH
                    // ─────────────────────────────────────────────────────────
                    composable(Screen.Splash.route) {
                        SplashScreen(
                            onStartClick = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Splash.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    // ─────────────────────────────────────────────────────────
                    // HOME
                    // ─────────────────────────────────────────────────────────
                    composable(Screen.Home.route) {
                        HomeScreen(
                            onTopicClick = { topicId ->
                                // topicId comes from HomeScreen's topic list (e.g. "math")
                                val title = topicId
                                    .replace("_", " ")
                                    .split(" ")
                                    .joinToString(" ") { it.replaceFirstChar(Char::uppercase) } +
                                        " Quiz"
                                navController.navigate(Screen.Quiz.createRoute(topicId, title))
                            },
                            onDailyQuizClick = {
                                navController.navigate(
                                    Screen.Quiz.createRoute("technology", "Daily Quiz")
                                )
                            },
                            onProfileClick = {
                                navController.navigate(Screen.Profile.route)
                            }
                        )
                    }

                    // ─────────────────────────────────────────────────────────
                    // TOPICS
                    // ─────────────────────────────────────────────────────────
                    composable(Screen.Topics.route) {
                        TopicScreen(
                            onTopicClick = { topicItem ->
                                val title = topicItem.title + " Quiz"
                                navController.navigate(
                                    Screen.Quiz.createRoute(topicItem.id, title)
                                )
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // ─────────────────────────────────────────────────────────
                    // PROFILE
                    // ─────────────────────────────────────────────────────────
                    composable(Screen.Profile.route) {
                        // Derive stats from ROOM results so the profile is live
                        val quizzesCompleted = allResults.size
                        val bestScore = if (allResults.isEmpty()) {
                            "N/A"
                        } else {
                            val best = allResults.maxByOrNull { it.score }!!
                            "${best.score}/${best.totalQuestions}"
                        }

                        ProfileScreen(
                            quizzesCompleted = quizzesCompleted,
                            bestScore        = bestScore,
                            onBack           = { navController.popBackStack() },
                            onHistoryClick   = {
                                navController.navigate(Screen.History.route)
                            }
                        )
                    }

                    // ─────────────────────────────────────────────────────────
                    // QUIZ
                    // ─────────────────────────────────────────────────────────
                    composable(
                        route     = Screen.Quiz.route,
                        arguments = listOf(
                            navArgument("topicId")    { type = NavType.StringType },
                            navArgument("topicTitle") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val topicId    = backStackEntry.arguments?.getString("topicId")    ?: ""
                        val topicTitle = backStackEntry.arguments?.getString("topicTitle") ?: ""

                        val questions by viewModel.questions.collectAsStateWithLifecycle()

                        // Capture the moment this quiz started — used to compute timeTaken
                        val startTime = remember { System.currentTimeMillis() }

                        LaunchedEffect(topicId) {
                            viewModel.clearQuestions()
                            viewModel.loadQuestions(topicId)
                        }

                        if (questions.isNotEmpty()) {
                            QuizScreen(
                                questions      = questions,
                                topicTitle     = topicTitle,
                                onBackClick    = { navController.popBackStack() },
                                onQuizFinished = { score, total, reviewItems ->
                                    // Build the "m:ss" duration string
                                    val elapsedMs = System.currentTimeMillis() - startTime
                                    val mins      = (elapsedMs / 60_000).toInt()
                                    val secs      = ((elapsedMs % 60_000) / 1_000).toInt()
                                    val timeTaken = "$mins:${secs.toString().padStart(2, '0')}"

                                    // Persist to ROOM; savedResultId updates when insert completes
                                    viewModel.saveResult(
                                        topic          = topicTitle,
                                        score          = score,
                                        totalQuestions = total,
                                        timeTaken      = timeTaken,
                                        reviewItems    = reviewItems
                                    )

                                    navController.navigate(
                                        Screen.Result.createRoute(score, total, timeTaken, topicTitle)
                                    ) {
                                        // Remove Quiz from back stack so Back on Result goes Home
                                        popUpTo(Screen.Quiz.route) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }

                    // ─────────────────────────────────────────────────────────
                    // RESULT
                    // ─────────────────────────────────────────────────────────
                    composable(
                        route     = Screen.Result.route,
                        arguments = listOf(
                            navArgument("score")      { type = NavType.IntType    },
                            navArgument("total")      { type = NavType.IntType    },
                            navArgument("timeTaken")  { type = NavType.StringType },
                            navArgument("topicTitle") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val score      = backStackEntry.arguments?.getInt("score")         ?: 0
                        val total      = backStackEntry.arguments?.getInt("total")         ?: 10
                        val timeTaken  = backStackEntry.arguments?.getString("timeTaken")  ?: "0:00"
                        val topicTitle = backStackEntry.arguments?.getString("topicTitle") ?: ""

                        // savedResultId is emitted by ViewModel once ROOM insert finishes
                        val resultId by viewModel.savedResultId.collectAsStateWithLifecycle()

                        ResultScreen(
                            score               = score,
                            totalQuestions      = total,
                            timeTakenText       = timeTaken,
                            topicTitle          = topicTitle,
                            onReviewLessonClick = {
                                resultId?.let { id ->
                                    navController.navigate(Screen.Review.createRoute(id))
                                }
                            },
                            onRestartQuizClick  = {
                                viewModel.clearSavedResultId()
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { inclusive = false }
                                }
                            },
                            onBackHomeClick     = {
                                viewModel.clearSavedResultId()
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { inclusive = false }
                                }
                            }
                        )
                    }

                    // ─────────────────────────────────────────────────────────
                    // REVIEW  (reached from ResultScreen → "Review Lesson")
                    // ─────────────────────────────────────────────────────────
                    composable(
                        route     = Screen.Review.route,
                        arguments = listOf(
                            navArgument("resultId") { type = NavType.LongType }
                        )
                    ) { backStackEntry ->
                        val resultId = backStackEntry.arguments?.getLong("resultId") ?: 0L

                        LaunchedEffect(resultId) {
                            viewModel.clearDetail()
                            viewModel.loadDetail(resultId)
                        }

                        val entity by viewModel.detailResult.collectAsStateWithLifecycle()

                        entity?.let { e ->
                            ReviewScreen(
                                topicTitle         = e.topic,
                                score              = e.score,
                                totalQuestions     = e.totalQuestions,
                                reviewItems        = parseReviewItems(e.reviewJson),
                                onRestartQuizClick = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Home.route) { inclusive = false }
                                    }
                                },
                                onBackHomeClick    = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Home.route) { inclusive = false }
                                    }
                                }
                            )
                        }
                    }

                    // ─────────────────────────────────────────────────────────
                    // HISTORY
                    // ─────────────────────────────────────────────────────────
                    composable(Screen.History.route) {
                        // Map ROOM entities → HistoryScreen's UI model
                        val historyItems = allResults.map { e ->
                            QuizHistoryItem(
                                id    = e.id.toInt(),
                                topic = e.topic.removeSuffix(" Quiz"),
                                date  = e.date,
                                score = "${e.score}/${e.totalQuestions}"
                            )
                        }

                        HistoryScreen(
                            onBackHome         = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { inclusive = false }
                                }
                            },
                            onTopicsClick      = {
                                navController.navigate(Screen.Topics.route)
                            },
                            onProfileClick     = {
                                navController.navigate(Screen.Profile.route)
                            },
                            onHistoryItemClick = { item ->
                                navController.navigate(
                                    Screen.HistoryDetail.createRoute(item.id.toLong())
                                )
                            }
                        )
                    }

                    // ─────────────────────────────────────────────────────────
                    // HISTORY DETAIL
                    // ─────────────────────────────────────────────────────────
                    composable(
                        route     = Screen.HistoryDetail.route,
                        arguments = listOf(
                            navArgument("resultId") { type = NavType.LongType }
                        )
                    ) { backStackEntry ->
                        val resultId = backStackEntry.arguments?.getLong("resultId") ?: 0L

                        LaunchedEffect(resultId) {
                            viewModel.clearDetail()
                            viewModel.loadDetail(resultId)
                        }

                        val entity by viewModel.detailResult.collectAsStateWithLifecycle()

                        entity?.let { e ->
                            HistoryDetailScreen(
                                detail = QuizHistoryDetail(
                                    historyItem = QuizHistoryItem(
                                        id    = e.id.toInt(),
                                        topic = e.topic.removeSuffix(" Quiz"),
                                        date  = e.date,
                                        score = "${e.score}/${e.totalQuestions}"
                                    ),
                                    timeTaken      = e.timeTaken,
                                    totalQuestions = e.totalQuestions,
                                    reviewItems    = parseReviewItems(e.reviewJson)
                                ),
                                onBackClick       = { navController.popBackStack() },
                                onRetakeQuizClick = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Home.route) { inclusive = false }
                                    }
                                },
                                onBackHomeClick   = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Home.route) { inclusive = false }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

/**
 * Deserializes [QuizResultEntity.reviewJson] back into a typed list.
 * Returns an empty list safely if the JSON is null or malformed.
 */
private fun parseReviewItems(jsonString: String?): List<ReviewQuestionItem> {
    if (jsonString.isNullOrBlank()) return emptyList()
    return try {
        Json { ignoreUnknownKeys = true }.decodeFromString(jsonString)
    } catch (e: Exception) {
        emptyList()
    }
}