package com.dma.studentapplication.data.local.room

import android.content.Context
import androidx.room.*

@Database(
    entities = [QuizResultEntity::class],
    version  = 1,
    exportSchema = false
)
abstract class QuizDatabase : RoomDatabase() {

    abstract fun quizResultDao(): QuizResultDao

    companion object {
        @Volatile private var INSTANCE: QuizDatabase? = null

        fun getInstance(context: Context): QuizDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    QuizDatabase::class.java,
                    "quiz_database"
                ).build().also { INSTANCE = it }
            }
    }
}
