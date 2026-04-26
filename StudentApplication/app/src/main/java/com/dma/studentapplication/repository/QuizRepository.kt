package com.dma.studentapplication.repository

import android.content.Context
import com.dma.studentapplication.data.local.room.QuizResultDao
import com.dma.studentapplication.data.local.room.QuizResultEntity
import com.dma.studentapplication.ui.screens.model.QuizQuestionUi
import com.dma.studentapplication.ui.screens.model.ReviewQuestionItem
import com.dma.studentapplication.utils.JsonLoader
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QuizRepository(
    private val dao: QuizResultDao,
    private val context: Context
) {

    private val json = Json { ignoreUnknownKeys = true }

    // ── Questions ─────────────────────────────────────────────────────────────

    fun getQuestions(topicId: String): List<QuizQuestionUi> =
        JsonLoader.getPreparedQuestions(context, topicId)

    // ── Persist a completed quiz ──────────────────────────────────────────────

    suspend fun saveResult(
        topic: String,
        score: Int,
        totalQuestions: Int,
        timeTaken: String,
        reviewItems: List<ReviewQuestionItem>
    ): Long {
        val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        val entity = QuizResultEntity(
            topic          = topic,
            date           = today,
            score          = score,
            totalQuestions = totalQuestions,
            timeTaken      = timeTaken,
            reviewJson     = json.encodeToString(reviewItems)   // kotlinx.serialization
        )
        return dao.insert(entity)
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    fun getAllResults(): Flow<List<QuizResultEntity>> = dao.getAllResults()

    suspend fun getResultById(id: Long): QuizResultEntity? = dao.getById(id)

    suspend fun clearAll() = dao.deleteAll()
}