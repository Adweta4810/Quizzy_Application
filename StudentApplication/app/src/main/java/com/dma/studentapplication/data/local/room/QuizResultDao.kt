package com.dma.studentapplication.data.local.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: QuizResultEntity)

    /** All results, newest first. */
    @Query("SELECT * FROM quiz_results ORDER BY dateTaken DESC")
    fun getAllResults(): Flow<List<QuizResultEntity>>

    /** Results filtered by topic, newest first. */
    @Query("SELECT * FROM quiz_results WHERE topicId = :topicId ORDER BY dateTaken DESC")
    fun getResultsByTopic(topicId: String): Flow<List<QuizResultEntity>>

    /** Best score ever for a given topic. */
    @Query("SELECT MAX(score) FROM quiz_results WHERE topicId = :topicId")
    suspend fun bestScoreForTopic(topicId: String): Int?

    /** Total XP across all attempts. */
    @Query("SELECT COALESCE(SUM(xpEarned), 0) FROM quiz_results")
    fun totalXp(): Flow<Int>

    @Query("DELETE FROM quiz_results")
    suspend fun clearAll()
}
