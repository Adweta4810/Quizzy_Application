package com.dma.studentapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dma.studentapplication.ui.screens.history.HistoryDetailScreen
import com.dma.studentapplication.ui.screens.history.HistoryScreen
import com.dma.studentapplication.ui.screens.home.HomeScreen
import com.dma.studentapplication.ui.screens.welcome.NickName
import com.dma.studentapplication.ui.screens.history.QuizHistoryDetail
import com.dma.studentapplication.ui.screens.history.QuizHistoryItem
import com.dma.studentapplication.ui.screens.profile.LeaderboardScreen
import com.dma.studentapplication.ui.screens.profile.ProfileScreen
import com.dma.studentapplication.ui.screens.quiz.QuizScreen
import com.dma.studentapplication.ui.screens.quiz.ResultScreen
import com.dma.studentapplication.ui.screens.review.ReviewQuestionItem
import com.dma.studentapplication.ui.screens.review.ReviewScreen
import com.dma.studentapplication.ui.screens.topic.TopicScreen
import com.dma.studentapplication.ui.screens.welcome.SplashScreen
import com.dma.studentapplication.viewmodel.QuizViewModel
import kotlinx.serialization.json.Json

/**
 * Main navigation graph for the whole application.
 *
 * This file keeps all route-to-screen mapping in one place.
 * MainActivity should only handle app setup, database setup, repository setup,
 * ViewModel setup, theme setup, and then call this AppNavGraph.
 *
 * This version also stores a user nickname entered from NicknameScreen.
 * That nickname is passed into HomeScreen and ProfileScreen so the app no longer
 * uses the hardcoded "Astrea" name.
 */
@Composable
fun AppNavGraph(
    viewModel: QuizViewModel
) {
    val navController = rememberNavController()

    /**
     * Collects all saved quiz results from the ViewModel.
     *
     * These results are used by:
     * - HomeScreen to show the last quiz
     * - ProfileScreen to show completed quiz count and best score
     * - HistoryScreen to show saved attempts
     * - LeaderboardScreen to rank scores
     */
    val allResults by viewModel.allResults.collectAsStateWithLifecycle()

    /**
     * Stores the nickname entered by the user.
     *
     * rememberSaveable keeps the value after recomposition and screen rotation.
     * Default is empty until the user enters a name on NicknameScreen.
     */
    var userName by rememberSaveable {
        mutableStateOf("")
    }

    /**
     * Handles bottom navigation between Home, Topics, History, and Profile.
     *
     * saveState/restoreState helps preserve tab state.
     * launchSingleTop prevents the same screen being added many times.
     */
    fun navigateBottomTab(route: String) {
        navController.navigate(route) {
            popUpTo(Screen.Home.route) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    /**
     * Navigates back to Home and clears the old Home destination.
     *
     * Used by:
     * - ResultScreen Back Home button
     * - ReviewScreen Back Home button
     * - HistoryDetailScreen Back Home button
     */
    fun goHome() {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    /**
     * Converts the saved quiz title back into the topic id used by JSON loading.
     *
     * Example:
     * "Programming Quiz" becomes "programming"
     * "Current Affairs Quiz" becomes "current_affairs"
     */
    fun topicTitleToId(topicTitle: String): String {
        return when (topicTitle) {
            "Daily Quiz" -> "technology"
            else -> topicTitle
                .removeSuffix(" Quiz")
                .lowercase()
                .replace(" ", "_")
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        /**
         * Splash screen.
         *
         * This is the first screen shown when the app opens.
         * After pressing Start Learning, the user goes to NicknameScreen,
         * not directly to HomeScreen.
         */
        composable(Screen.Splash.route) {
            SplashScreen(
                onStartClick = {
                    navController.navigate(Screen.Nickname.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        /**
         * Nickname screen.
         *
         * Collects the user's nickname and saves it in [userName].
         * After saving, it navigates to HomeScreen and removes NicknameScreen
         * from the back stack so the user cannot go back to it accidentally.
         */
        composable(Screen.Nickname.route) {
            NickName(
                onContinueClick = { enteredName ->
                    userName = enteredName

                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Nickname.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        /**
         * Home screen.
         *
         * Shows:
         * - User greeting using the entered nickname
         * - Last quiz result
         * - Topic cards
         * - Bottom navigation
         */
        composable(Screen.Home.route) {
            val lastResult = allResults.maxByOrNull { it.id }

            HomeScreen(
                userName = userName.ifBlank { "Student" },
                lastQuizTopic = lastResult?.topic,
                lastQuizScore = lastResult?.score,
                lastQuizTotal = lastResult?.totalQuestions,
                onTopicClick = { topicId ->
                    val title = topicId
                        .replace("_", " ")
                        .split(" ")
                        .joinToString(" ") {
                            it.replaceFirstChar(Char::uppercase)
                        } + " Quiz"

                    navController.navigate(
                        Screen.Quiz.createRoute(topicId, title)
                    )
                },
                onDailyQuizClick = {
                    navController.navigate(
                        Screen.Quiz.createRoute("technology", "Daily Quiz")
                    )
                },
                onTopicsClick = {
                    navigateBottomTab(Screen.Topics.route)
                },
                onHistoryClick = {
                    navigateBottomTab(Screen.History.route)
                },
                onProfileClick = {
                    navigateBottomTab(Screen.Profile.route)
                }
            )
        }

        /**
         * Topics screen.
         *
         * Displays all quiz topics.
         * Tapping a topic starts a quiz for that topic.
         */
        composable(Screen.Topics.route) {
            TopicScreen(
                onTopicClick = { topicItem ->
                    navController.navigate(
                        Screen.Quiz.createRoute(
                            topicItem.id,
                            "${topicItem.title} Quiz"
                        )
                    )
                },
                onBack = {
                    goHome()
                },
                onHomeClick = {
                    navigateBottomTab(Screen.Home.route)
                },
                onHistoryClick = {
                    navigateBottomTab(Screen.History.route)
                },
                onProfileClick = {
                    navigateBottomTab(Screen.Profile.route)
                }
            )
        }

        /**
         * Profile screen.
         *
         * Uses the entered nickname instead of the old hardcoded name.
         * Quiz statistics are calculated from saved Room database results.
         */
        composable(Screen.Profile.route) {
            val quizzesCompleted = allResults.size

            val bestScore = allResults
                .maxByOrNull { it.score }
                ?.let { "${it.score}/${it.totalQuestions}" }
                ?: "N/A"

            val displayName = userName.ifBlank { "Student" }

            ProfileScreen(
                userName = displayName,
                quizzesCompleted = quizzesCompleted,
                bestScore = bestScore,
                onBack = {
                    goHome()
                },
                onHomeClick = {
                    navigateBottomTab(Screen.Home.route)
                },
                onTopicsClick = {
                    navigateBottomTab(Screen.Topics.route)
                },
                onHistoryClick = {
                    navigateBottomTab(Screen.History.route)
                },
                onLeaderboardClick = {
                    navController.navigate(Screen.Leaderboard.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        /**
         * Quiz screen.
         *
         * Loads questions by topic id.
         * Tracks the quiz start time so the result screen can show time taken.
         */
        composable(
            route = Screen.Quiz.route,
            arguments = listOf(
                navArgument("topicId") {
                    type = NavType.StringType
                },
                navArgument("topicTitle") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val topicId = backStackEntry.arguments?.getString("topicId") ?: ""
            val topicTitle = backStackEntry.arguments?.getString("topicTitle") ?: ""

            /**
             * Captures quiz start time once for this quiz route.
             *
             * remember(topicId, topicTitle) prevents this from resetting on normal
             * recomposition but creates a new start time for a new quiz.
             */
            val startTime = remember(topicId, topicTitle) {
                System.currentTimeMillis()
            }

            /**
             * Loads questions when the topic id changes.
             *
             * Your ViewModel should guard against unnecessary reshuffling,
             * especially during rotation.
             */
            LaunchedEffect(topicId) {
                viewModel.loadQuestions(topicId)
            }

            QuizScreen(
                viewModel = viewModel,
                topicTitle = topicTitle,
                onBackClick = {
                    navController.popBackStack()
                },
                onQuizFinished = { score, total, reviewItems ->

                    val elapsedMs = System.currentTimeMillis() - startTime
                    val mins = (elapsedMs / 60_000).toInt()
                    val secs = ((elapsedMs % 60_000) / 1_000).toInt()
                    val timeTaken = "$mins:${secs.toString().padStart(2, '0')}"

                    viewModel.saveResult(
                        topic = topicTitle,
                        score = score,
                        totalQuestions = total,
                        timeTaken = timeTaken,
                        reviewItems = reviewItems
                    ) { resultId ->

                        navController.navigate(
                            Screen.Result.createRoute(
                                resultId = resultId,
                                score = score,
                                total = total,
                                timeTaken = timeTaken,
                                topicTitle = topicTitle
                            )
                        ) {
                            popUpTo(Screen.Quiz.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        /**
         * Result screen.
         *
         * Shows score, time taken, topic title, review button, restart button,
         * and back home button.
         */
        composable(
            route = Screen.Result.route,
            arguments = listOf(
                navArgument("resultId") {
                    type = NavType.LongType
                },
                navArgument("score") {
                    type = NavType.IntType
                },
                navArgument("total") {
                    type = NavType.IntType
                },
                navArgument("timeTaken") {
                    type = NavType.StringType
                },
                navArgument("topicTitle") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val resultId = backStackEntry.arguments?.getLong("resultId") ?: 0L
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 10
            val timeTaken = backStackEntry.arguments?.getString("timeTaken") ?: "0:00"
            val topicTitle = backStackEntry.arguments?.getString("topicTitle") ?: ""

            ResultScreen(
                score = score,
                totalQuestions = total,
                timeTakenText = timeTaken,
                topicTitle = topicTitle,
                onReviewLessonClick = {
                    navController.navigate(
                        Screen.Review.createRoute(resultId)
                    )
                },
                onRestartQuizClick = {
                    viewModel.clearSavedResultId()

                    navController.navigate(
                        Screen.Quiz.createRoute(
                            topicTitleToId(topicTitle),
                            topicTitle
                        )
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

        /**
         * Review screen.
         *
         * Loads one saved quiz result by id and displays all reviewed answers.
         */
        composable(
            route = Screen.Review.route,
            arguments = listOf(
                navArgument("resultId") {
                    type = NavType.LongType
                }
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
                    topicTitle = e.topic,
                    score = e.score,
                    totalQuestions = e.totalQuestions,
                    reviewItems = parseReviewItems(e.reviewJson),
                    onRestartQuizClick = {
                        viewModel.clearSavedResultId()

                        navController.navigate(
                            Screen.Quiz.createRoute(
                                topicTitleToId(e.topic),
                                e.topic
                            )
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

        /**
         * History screen.
         *
         * Converts Room entities into QuizHistoryItem objects for display.
         */
        composable(Screen.History.route) {
            val historyItems = allResults.map { e ->
                QuizHistoryItem(
                    id = e.id.toInt(),
                    topic = e.topic.removeSuffix(" Quiz"),
                    date = e.date,
                    score = "${e.score}/${e.totalQuestions}"
                )
            }

            HistoryScreen(
                historyItems = historyItems,
                onBackHome = {
                    navigateBottomTab(Screen.Home.route)
                },
                onTopicsClick = {
                    navigateBottomTab(Screen.Topics.route)
                },
                onProfileClick = {
                    navigateBottomTab(Screen.Profile.route)
                },
                onHistoryItemClick = { item ->
                    navController.navigate(
                        Screen.HistoryDetail.createRoute(item.id.toLong())
                    )
                },
                onClearHistory = {
                    viewModel.clearAllHistory()
                }
            )
        }

        /**
         * Leaderboard screen.
         *
         * Uses saved quiz history and sorts it inside LeaderboardScreen.
         */
        composable(Screen.Leaderboard.route) {
            val historyItems = allResults.map { e ->
                QuizHistoryItem(
                    id = e.id.toInt(),
                    topic = e.topic.removeSuffix(" Quiz"),
                    date = e.date,
                    score = "${e.score}/${e.totalQuestions}"
                )
            }

            LeaderboardScreen(
                leaderboardItems = historyItems,
                onBackHome = {
                    navigateBottomTab(Screen.Home.route)
                },
                onTopicsClick = {
                    navigateBottomTab(Screen.Topics.route)
                },
                onHistoryClick = {
                    navigateBottomTab(Screen.History.route)
                },
                onProfileClick = {
                    navigateBottomTab(Screen.Profile.route)
                }
            )
        }

        /**
         * History detail screen.
         *
         * Shows full details for one saved quiz result, including answer review.
         */
        composable(
            route = Screen.HistoryDetail.route,
            arguments = listOf(
                navArgument("resultId") {
                    type = NavType.LongType
                }
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
                            id = e.id.toInt(),
                            topic = e.topic.removeSuffix(" Quiz"),
                            date = e.date,
                            score = "${e.score}/${e.totalQuestions}"
                        ),
                        timeTaken = e.timeTaken,
                        totalQuestions = e.totalQuestions,
                        reviewItems = parseReviewItems(e.reviewJson)
                    ),
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onRetakeQuizClick = {
                        viewModel.clearSavedResultId()

                        navController.navigate(
                            Screen.Quiz.createRoute(
                                topicTitleToId(e.topic),
                                e.topic
                            )
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
    }
}

/**
 * Converts saved review JSON back into a list of ReviewQuestionItem.
 *
 * This is used by:
 * - ReviewScreen
 * - HistoryDetailScreen
 *
 * If JSON is missing, blank, or invalid, this function returns an empty list
 * instead of crashing the app.
 */
private fun parseReviewItems(jsonString: String?): List<ReviewQuestionItem> {
    if (jsonString.isNullOrBlank()) {
        return emptyList()
    }

    return try {
        Json {
            ignoreUnknownKeys = true
        }.decodeFromString<List<ReviewQuestionItem>>(jsonString)
    } catch (_: Exception) {
        emptyList()
    }
}