package com.dma.studentapplication.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {

    /**
     * Inserts a new quiz result and returns its auto-generated row id.
     * The id is immediately passed to the Result screen so "Review Lesson"
     * can navigate to the correct ROOM record.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: QuizResultEntity): Long

    /**
     * Returns all quiz results ordered newest-first as a [Flow] so
     * [com.dma.studentapplication.ui.screens.history.HistoryScreen] updates automatically when new results are saved.
     */
    @Query("SELECT * FROM quiz_results ORDER BY id DESC")
    fun getAllResults(): Flow<List<QuizResultEntity>>

    /**
     * Returns a single result by its primary key — used by [com.dma.studentapplication.ui.screens.review.ReviewScreen]
     * and [com.dma.studentapplication.ui.screens.history.HistoryDetailScreen] to load per-question breakdown data.
     */
    @Query("SELECT * FROM quiz_results WHERE id = :id")
    suspend fun getById(id: Long): QuizResultEntity?

    /** Wipes all stored results (useful for a "Clear History" feature). */
    @Query("DELETE FROM quiz_results")
    suspend fun deleteAll()
}