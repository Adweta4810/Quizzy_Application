package com.dma.studentapplication.utils

import android.content.Context
import com.dma.studentapplication.model.Question
import com.dma.studentapplication.ui.model.QuizQuestionUi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

object JsonLoader {

    private val json = Json { ignoreUnknownKeys = true }

    /** Maps every topic id → its asset filename. */
    private val topicFileMap = mapOf(
        "math"            to "math.json",
        "history"         to "history.json",
        "science"         to "science.json",
        "programming"     to "programming.json",
        "movies"          to "movies.json",
        "sports"          to "sports.json",
        "geography"       to "geography.json",
        "networking"      to "networking.json",
        "technology"      to "technology.json",
        "current_affairs" to "current_affairs.json"
    )

    /** Loads all questions from a given asset file. */
    fun loadQuestions(context: Context, fileName: String): List<Question> {
        return try {
            val raw = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val element = json.parseToJsonElement(raw)

            val questionArray: JsonArray = when {
                element is JsonArray -> element
                element.jsonObject.containsKey("questions") ->
                    element.jsonObject["questions"]!!.jsonArray
                else -> JsonArray(emptyList())
            }

            json.decodeFromJsonElement<List<Question>>(questionArray)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Returns exactly 10 shuffled [QuizQuestionUi] ready for the quiz.
     * Both the question pool and each question's options are shuffled
     * so answers are never in the same position.
     */
    fun getPreparedQuestions(context: Context, topicId: String): List<QuizQuestionUi> {
        val fileName = topicFileMap[topicId] ?: return emptyList()
        return loadQuestions(context, fileName)
            .shuffled()
            .take(10)
            .mapIndexed { index, question ->
                val correctText  = question.options[question.correctAnswerIndex]
                val shuffledOpts = question.options.shuffled()
                QuizQuestionUi(
                    id            = index + 1,
                    question      = question.questionText,
                    options       = shuffledOpts,
                    correctAnswer = correctText
                )
            }
    }
}