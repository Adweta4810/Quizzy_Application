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
import com.dma.studentapplication.ui.screens.model.QuizHistoryDetail
import com.dma.studentapplication.ui.screens.model.ReviewQuestionItem
import com.dma.studentapplication.ui.theme.StudentApplicationTheme
import com.dma.studentapplication.viewmodel.QuizViewModel
import com.dma.studentapplication.viewmodel.QuizViewModelFactory
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = QuizDatabase.getInstance(this)
        val repository = QuizRepository(db.quizResultDao(), this)
        val vmFactory = QuizViewModelFactory(repository)

        setContent {
            StudentApplicationTheme {
                val navController = rememberNavController()
                val viewModel: QuizViewModel = viewModel(factory = vmFactory)
                val allResults by viewModel.allResults.collectAsStateWithLifecycle()

                fun navigateBottomTab(route: String) {
                    navController.navigate(route) {
                        popUpTo(Screen.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

                fun goHome() {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }

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
                    composable(Screen.Splash.route) {
                        SplashScreen(
                            onStartClick = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Splash.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }

                    composable(Screen.Home.route) {
                        HomeScreen(
                            onTopicClick = { topicId ->
                                val title = topicId
                                    .replace("_", " ")
                                    .split(" ")
                                    .joinToString(" ") {
                                        it.replaceFirstChar(Char::uppercase)
                                    } + " Quiz"

                                navController.navigate(Screen.Quiz.createRoute(topicId, title))
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

                    composable(Screen.Profile.route) {
                        val quizzesCompleted = allResults.size
                        val bestScore = allResults.maxByOrNull { it.score }
                            ?.let { "${it.score}/${it.totalQuestions}" } ?: "N/A"

                        ProfileScreen(
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

                    composable(
                        route = Screen.Quiz.route,
                        arguments = listOf(
                            navArgument("topicId") { type = NavType.StringType },
                            navArgument("topicTitle") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val topicId = backStackEntry.arguments?.getString("topicId") ?: ""
                        val topicTitle = backStackEntry.arguments?.getString("topicTitle") ?: ""
                        val startTime = remember(topicId, topicTitle) {
                            System.currentTimeMillis()
                        }

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
                                            resultId,
                                            score,
                                            total,
                                            timeTaken,
                                            topicTitle
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

                    composable(
                        route = Screen.Result.route,
                        arguments = listOf(
                            navArgument("resultId") { type = NavType.LongType },
                            navArgument("score") { type = NavType.IntType },
                            navArgument("total") { type = NavType.IntType },
                            navArgument("timeTaken") { type = NavType.StringType },
                            navArgument("topicTitle") { type = NavType.StringType }
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
                                navController.navigate(Screen.Review.createRoute(resultId))
                            },
                            onRestartQuizClick = {
                                viewModel.clearSavedResultId()
                                val topicId = topicTitleToId(topicTitle)

                                navController.navigate(
                                    Screen.Quiz.createRoute(topicId, topicTitle)
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

                    composable(
                        route = Screen.Review.route,
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
                                topicTitle = e.topic,
                                score = e.score,
                                totalQuestions = e.totalQuestions,
                                reviewItems = parseReviewItems(e.reviewJson),
                                onRestartQuizClick = {
                                    viewModel.clearSavedResultId()
                                    val topicId = topicTitleToId(e.topic)

                                    navController.navigate(
                                        Screen.Quiz.createRoute(topicId, e.topic)
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
                            }
                        )
                    }

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

                    composable(
                        route = Screen.HistoryDetail.route,
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
                                    val topicId = topicTitleToId(e.topic)

                                    navController.navigate(
                                        Screen.Quiz.createRoute(topicId, e.topic)
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
        }
    }
}

private fun parseReviewItems(jsonString: String?): List<ReviewQuestionItem> {
    if (jsonString.isNullOrBlank()) return emptyList()

    return try {
        Json {
            ignoreUnknownKeys = true
        }.decodeFromString<List<ReviewQuestionItem>>(jsonString)
    } catch (_: Exception) {
        emptyList()
    }
}