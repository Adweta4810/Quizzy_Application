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
import com.dma.studentapplication.ui.screens.*
import com.dma.studentapplication.data.local.room.*
import com.dma.studentapplication.ui.screens.model.QuizHistoryDetail
import com.dma.studentapplication.ui.screens.model.ReviewQuestionItem
import com.dma.studentapplication.ui.theme.StudentApplicationTheme
import com.dma.studentapplication.viewmodel.QuizViewModel
import com.dma.studentapplication.viewmodel.QuizViewModelFactory
import kotlinx.serialization.json.Json

/**
 * Single Activity that hosts the entire app.
 *
 * Responsibilities:
 * - Bootstraps the Room database, repository, and ViewModel factory
 * - Sets the Compose content tree inside [StudentApplicationTheme]
 * - Owns the [NavHost] and maps every [Screen] route to its composable
 * - Passes navigation callbacks down to each screen so screens stay
 *   decoupled from the nav controller
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Draw content edge-to-edge behind system bars
        enableEdgeToEdge()

        // ── Dependency setup ──────────────────────────────────────────────────
        // Manual DI: build the object graph once here and pass it down.
        // Replace with Hilt/Koin if the project grows.
        val db         = QuizDatabase.getInstance(this)
        val repository = QuizRepository(db.quizResultDao(), this)
        val vmFactory  = QuizViewModelFactory(repository)

        setContent {
            StudentApplicationTheme {
                val navController = rememberNavController()

                // Single shared ViewModel scoped to the Activity so all screens
                // see the same question list and history data.
                val viewModel: QuizViewModel = viewModel(factory = vmFactory)

                // Collect the full history list; used by Home, Profile, History,
                // and Leaderboard screens.
                val allResults by viewModel.allResults.collectAsStateWithLifecycle()

                // ── Navigation helpers ────────────────────────────────────────

                /**
                 * Navigate to a bottom-nav tab while:
                 * - Popping back to Home so the back stack doesn't grow unboundedly
                 * - Saving and restoring tab state across switches
                 * - Preventing duplicate destinations on the stack
                 */
                fun navigateBottomTab(route: String) {
                    navController.navigate(route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState    = true
                    }
                }

                /**
                 * Navigate to Home and clear the entire back stack up to and
                 * including the Home destination, then re-create it.
                 * Used by "Back Home" buttons on result, review, and detail screens.
                 */
                fun goHome() {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }

                /**
                 * Converts a human-readable topic title back to its snake_case id.
                 *
                 * Examples:
                 * - "Technology Quiz" → "technology"
                 * - "Current Affairs Quiz" → "current_affairs"
                 * - "Daily Quiz" → "technology" (special case for the home daily quiz)
                 */
                fun topicTitleToId(topicTitle: String): String {
                    return when (topicTitle) {
                        "Daily Quiz" -> "technology"
                        else         -> topicTitle
                            .removeSuffix(" Quiz")
                            .lowercase()
                            .replace(" ", "_")
                    }
                }

                // ── Nav graph ─────────────────────────────────────────────────
                NavHost(
                    navController    = navController,
                    startDestination = Screen.Splash.route
                ) {

                    // ── Splash ────────────────────────────────────────────────
                    // Entry point of the app. Navigates to Home and removes
                    // itself from the back stack so Back doesn't return here.
                    composable(Screen.Splash.route) {
                        SplashScreen(
                            onStartClick = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Splash.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    // ── Home ──────────────────────────────────────────────────
                    // Shows the most recent quiz result as the "Last Quiz" card.
                    // Tapping a topic card or the daily quiz starts a new quiz session.
                    composable(Screen.Home.route) {
                        // Pick the result with the highest id (most recently inserted)
                        val lastResult = allResults.maxByOrNull { it.id }

                        HomeScreen(
                            lastQuizTopic = lastResult?.topic,
                            lastQuizScore = lastResult?.score,
                            lastQuizTotal = lastResult?.totalQuestions,
                            onTopicClick  = { topicId ->
                                // Convert snake_case id to a title-cased display name
                                val title = topicId
                                    .replace("_", " ")
                                    .split(" ")
                                    .joinToString(" ") { it.replaceFirstChar(Char::uppercase) } + " Quiz"

                                navController.navigate(Screen.Quiz.createRoute(topicId, title))
                            },
                            onDailyQuizClick = {
                                navController.navigate(
                                    Screen.Quiz.createRoute("technology", "Daily Quiz")
                                )
                            },
                            onTopicsClick  = { navigateBottomTab(Screen.Topics.route) },
                            onHistoryClick = { navigateBottomTab(Screen.History.route) },
                            onProfileClick = { navigateBottomTab(Screen.Profile.route) }
                        )
                    }

                    // ── Topics ────────────────────────────────────────────────
                    // Full topic list. Tapping a topic starts a quiz for that topic.
                    composable(Screen.Topics.route) {
                        TopicScreen(
                            onTopicClick = { topicItem ->
                                navController.navigate(
                                    Screen.Quiz.createRoute(topicItem.id, "${topicItem.title} Quiz")
                                )
                            },
                            onBack         = { goHome() },
                            onHomeClick    = { navigateBottomTab(Screen.Home.route) },
                            onHistoryClick = { navigateBottomTab(Screen.History.route) },
                            onProfileClick = { navigateBottomTab(Screen.Profile.route) }
                        )
                    }

                    // ── Profile ───────────────────────────────────────────────
                    // Derives stats (quiz count, best score) from the live history list.
                    composable(Screen.Profile.route) {
                        val quizzesCompleted = allResults.size
                        val bestScore        = allResults
                            .maxByOrNull { it.score }
                            ?.let { "${it.score}/${it.totalQuestions}" } ?: "N/A"

                        ProfileScreen(
                            quizzesCompleted   = quizzesCompleted,
                            bestScore          = bestScore,
                            onBack             = { goHome() },
                            onHomeClick        = { navigateBottomTab(Screen.Home.route) },
                            onTopicsClick      = { navigateBottomTab(Screen.Topics.route) },
                            onHistoryClick     = { navigateBottomTab(Screen.History.route) },
                            onLeaderboardClick = {
                                navController.navigate(Screen.Leaderboard.route) {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }

                    // ── Quiz ──────────────────────────────────────────────────
                    // Active quiz session. Questions are loaded once per topicId via
                    // LaunchedEffect. The start timestamp is captured with remember so
                    // it survives recomposition but resets when the route arguments change.
                    composable(
                        route     = Screen.Quiz.route,
                        arguments = listOf(
                            navArgument("topicId")    { type = NavType.StringType },
                            navArgument("topicTitle") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val topicId    = backStackEntry.arguments?.getString("topicId")    ?: ""
                        val topicTitle = backStackEntry.arguments?.getString("topicTitle") ?: ""

                        // Capture quiz start time once per unique (topicId, topicTitle) pair
                        val startTime = remember(topicId, topicTitle) {
                            System.currentTimeMillis()
                        }

                        // Load questions exactly once — QuizViewModel guards against reloads
                        LaunchedEffect(topicId) {
                            viewModel.loadQuestions(topicId)
                        }

                        QuizScreen(
                            viewModel  = viewModel,
                            topicTitle = topicTitle,
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onQuizFinished = { score, total, reviewItems ->
                                // Calculate elapsed time and format as "m:ss"
                                val elapsedMs = System.currentTimeMillis() - startTime
                                val mins      = (elapsedMs / 60_000).toInt()
                                val secs      = ((elapsedMs % 60_000) / 1_000).toInt()
                                val timeTaken = "$mins:${secs.toString().padStart(2, '0')}"

                                // Persist the result then navigate to the Result screen.
                                // The resultId is passed via the route so the Result screen
                                // can load the full review data from the database.
                                viewModel.saveResult(
                                    topic          = topicTitle,
                                    score          = score,
                                    totalQuestions = total,
                                    timeTaken      = timeTaken,
                                    reviewItems    = reviewItems
                                ) { resultId ->
                                    navController.navigate(
                                        Screen.Result.createRoute(
                                            resultId, score, total, timeTaken, topicTitle
                                        )
                                    ) {
                                        // Remove the quiz from the back stack — the user
                                        // should not be able to go back into an active quiz
                                        popUpTo(Screen.Quiz.route) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            }
                        )
                    }

                    // ── Result ────────────────────────────────────────────────
                    // Summary screen shown immediately after a quiz ends.
                    // All data is passed via route arguments to avoid an async load.
                    composable(
                        route     = Screen.Result.route,
                        arguments = listOf(
                            navArgument("resultId")   { type = NavType.LongType   },
                            navArgument("score")      { type = NavType.IntType    },
                            navArgument("total")      { type = NavType.IntType    },
                            navArgument("timeTaken")  { type = NavType.StringType },
                            navArgument("topicTitle") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val resultId   = backStackEntry.arguments?.getLong("resultId")       ?: 0L
                        val score      = backStackEntry.arguments?.getInt("score")            ?: 0
                        val total      = backStackEntry.arguments?.getInt("total")            ?: 10
                        val timeTaken  = backStackEntry.arguments?.getString("timeTaken")     ?: "0:00"
                        val topicTitle = backStackEntry.arguments?.getString("topicTitle")    ?: ""

                        ResultScreen(
                            score               = score,
                            totalQuestions      = total,
                            timeTakenText       = timeTaken,
                            topicTitle          = topicTitle,
                            onReviewLessonClick = {
                                // Pass resultId so ReviewScreen can load the full answer breakdown
                                navController.navigate(Screen.Review.createRoute(resultId))
                            },
                            onRestartQuizClick  = {
                                viewModel.clearSavedResultId()
                                navController.navigate(
                                    Screen.Quiz.createRoute(topicTitleToId(topicTitle), topicTitle)
                                ) {
                                    popUpTo(Screen.Home.route)
                                    launchSingleTop = true
                                }
                            },
                            onBackHomeClick     = {
                                viewModel.clearSavedResultId()
                                goHome()
                            }
                        )
                    }

                    // ── Review ────────────────────────────────────────────────
                    // Per-question answer breakdown for a completed quiz.
                    // Loads the result entity by id via the ViewModel then renders
                    // once the entity is available (entity?.let).
                    composable(
                        route     = Screen.Review.route,
                        arguments = listOf(
                            navArgument("resultId") { type = NavType.LongType }
                        )
                    ) { backStackEntry ->
                        val resultId = backStackEntry.arguments?.getLong("resultId") ?: 0L

                        // Clear any stale detail then load the requested result
                        LaunchedEffect(resultId) {
                            viewModel.clearDetail()
                            viewModel.loadDetail(resultId)
                        }

                        val entity by viewModel.detailResult.collectAsStateWithLifecycle()

                        // Render nothing until the entity arrives from the database
                        entity?.let { e ->
                            ReviewScreen(
                                topicTitle      = e.topic,
                                score           = e.score,
                                totalQuestions  = e.totalQuestions,
                                reviewItems     = parseReviewItems(e.reviewJson),
                                onRestartQuizClick = {
                                    viewModel.clearSavedResultId()
                                    navController.navigate(
                                        Screen.Quiz.createRoute(topicTitleToId(e.topic), e.topic)
                                    ) {
                                        popUpTo(Screen.Home.route)
                                        launchSingleTop = true
                                    }
                                },
                                onBackHomeClick = {
                                    viewModel.clearSavedResultId()
                                    goHome()
                                }
                            )
                        }
                    }

                    // ── History ───────────────────────────────────────────────
                    // Maps Room entities to the lightweight QuizHistoryItem UI model.
                    // The " Quiz" suffix is stripped from topic names saved by the quiz flow.
                    composable(Screen.History.route) {
                        val historyItems = allResults.map { e ->
                            QuizHistoryItem(
                                id    = e.id.toInt(),
                                topic = e.topic.removeSuffix(" Quiz"),
                                date  = e.date,
                                score = "${e.score}/${e.totalQuestions}"
                            )
                        }

                        HistoryScreen(
                            historyItems       = historyItems,
                            onBackHome         = { navigateBottomTab(Screen.Home.route) },
                            onTopicsClick      = { navigateBottomTab(Screen.Topics.route) },
                            onProfileClick     = { navigateBottomTab(Screen.Profile.route) },
                            onHistoryItemClick = { item ->
                                navController.navigate(
                                    Screen.HistoryDetail.createRoute(item.id.toLong())
                                )
                            }
                        )
                    }

                    // ── Leaderboard ───────────────────────────────────────────
                    // Same data mapping as History — sorting is done inside LeaderboardScreen.
                    composable(Screen.Leaderboard.route) {
                        val historyItems = allResults.map { e ->
                            QuizHistoryItem(
                                id    = e.id.toInt(),
                                topic = e.topic.removeSuffix(" Quiz"),
                                date  = e.date,
                                score = "${e.score}/${e.totalQuestions}"
                            )
                        }

                        LeaderboardScreen(
                            leaderboardItems = historyItems,
                            onBackHome       = { navigateBottomTab(Screen.Home.route) },
                            onTopicsClick    = { navigateBottomTab(Screen.Topics.route) },
                            onHistoryClick   = { navigateBottomTab(Screen.History.route) },
                            onProfileClick   = { navigateBottomTab(Screen.Profile.route) }
                        )
                    }

                    // ── History detail ────────────────────────────────────────
                    // Full detail view for a single history entry including the
                    // per-question answer breakdown. Uses the same loadDetail pattern
                    // as the Review screen.
                    composable(
                        route     = Screen.HistoryDetail.route,
                        arguments = listOf(
                            navArgument("resultId") { type = NavType.LongType }
                        )
                    ) { backStackEntry ->
                        val resultId = backStackEntry.arguments?.getLong("resultId") ?: 0L

                        // Clear stale detail then load the requested result
                        LaunchedEffect(resultId) {
                            viewModel.clearDetail()
                            viewModel.loadDetail(resultId)
                        }

                        val entity by viewModel.detailResult.collectAsStateWithLifecycle()

                        // Render nothing until the entity arrives from the database
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
                                    viewModel.clearSavedResultId()
                                    navController.navigate(
                                        Screen.Quiz.createRoute(topicTitleToId(e.topic), e.topic)
                                    ) {
                                        popUpTo(Screen.Home.route)
                                        launchSingleTop = true
                                    }
                                },
                                onBackHomeClick   = {
                                    viewModel.clearSavedResultId()
                                    goHome()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Utilities ─────────────────────────────────────────────────────────────────

/**
 * Deserializes the JSON string stored in [QuizResultEntity.reviewJson] back into
 * a list of [ReviewQuestionItem] objects for display on the review and detail screens.
 *
 * Returns an empty list if:
 * - [jsonString] is null or blank (no review data was saved)
 * - Deserialisation fails for any reason (corrupt data, schema mismatch)
 *
 * @param jsonString Raw JSON string from the database, or null.
 */
private fun parseReviewItems(jsonString: String?): List<ReviewQuestionItem> {
    if (jsonString.isNullOrBlank()) return emptyList()

    return try {
        Json { ignoreUnknownKeys = true }
            .decodeFromString<List<ReviewQuestionItem>>(jsonString)
    } catch (_: Exception) {
        // Swallow deserialization errors gracefully — the review section
        // will simply show no items rather than crashing the app.
        emptyList()
    }
}