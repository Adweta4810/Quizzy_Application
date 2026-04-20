package com.dma.studentapplication.repository

import android.content.Context
import com.dma.studentapplication.data.local.room.QuizDatabase
import com.dma.studentapplication.data.local.room.QuizResultEntity
import com.dma.studentapplication.model.AnsweredQuestion
import com.dma.studentapplication.model.Question
import com.dma.studentapplication.model.QuizResult
import com.dma.studentapplication.utils.JsonLoader
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Serialisable DTO for persisting one answered question.
 */
@Serializable
data class AnsweredQuestionDto(
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val selectedIndex: Int,
    val timeTaken: Int
)

class QuizRepository(context: Context) {

    private val dao = QuizDatabase.getInstance(context).quizResultDao()
    private val appContext = context.applicationContext
    private val json = Json { ignoreUnknownKeys = true }

    // ── Question loading ──────────────────────────────────────────────────────

    /**
     * Load questions from assets, shuffle them, and return the first [count].
     */
    fun loadQuestions(fileName: String, count: Int = 10): List<Question> =
        JsonLoader.loadQuestions(appContext, fileName).shuffled().take(count)

    // ── History persistence ───────────────────────────────────────────────────

    suspend fun saveResult(result: QuizResult) {
        val dtos = result.answers.map { aq ->
            AnsweredQuestionDto(
                questionText       = aq.question.questionText,
                options            = aq.question.options,
                correctAnswerIndex = aq.question.correctAnswerIndex,
                selectedIndex      = aq.selectedIndex,
                timeTaken          = aq.timeTaken
            )
        }
        val entity = QuizResultEntity(
            topicId          = result.topicId,
            topicTitle       = result.topicTitle,
            score            = result.score,
            totalQuestions   = result.totalQuestions,
            xpEarned         = result.xpEarned,
            timeTakenSeconds = result.timeTakenSeconds,
            answersJson      = json.encodeToString(dtos)
        )
        dao.insert(entity)
    }

    fun getAllResults(): Flow<List<QuizResultEntity>> = dao.getAllResults()

    fun getResultsByTopic(topicId: String): Flow<List<QuizResultEntity>> =
        dao.getResultsByTopic(topicId)

    fun totalXp(): Flow<Int> = dao.totalXp()

    suspend fun bestScoreForTopic(topicId: String): Int? = dao.bestScoreForTopic(topicId)

    /** Decode persisted JSON answers back into [AnsweredQuestion] objects. */
    fun decodeAnswers(entity: QuizResultEntity): List<AnsweredQuestion> {
        return try {
            json.decodeFromString<List<AnsweredQuestionDto>>(entity.answersJson).map { dto ->
                AnsweredQuestion(
                    question = Question(dto.questionText, dto.options, dto.correctAnswerIndex),
                    selectedIndex = dto.selectedIndex,
                    timeTaken = dto.timeTaken
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
