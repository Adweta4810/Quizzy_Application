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

    /**
     * Reads raw JSON from assets and returns all [Question] objects.
     *
     * Handles two formats:
     * - Bare array  : math.json            → `[ {...}, ... ]`
     * - Wrapped obj : all others           → `{ "questions": [ {...}, ... ] }`
     */
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
     * Returns exactly [Constants.QUESTIONS_PER_QUIZ] shuffled [QuizQuestionUi]
     * ready for the quiz.
     *
     * - Topic filename is resolved from [Constants.quizTopics] so there is
     *   a single source of truth for topic → file mapping.
     * - Both the question pool and each question's options are shuffled so
     *   answers are never in the same position every time.
     */
    fun getPreparedQuestions(context: Context, topicId: String): List<QuizQuestionUi> {
        val fileName = Constants.quizTopics
            .find { it.id == topicId }?.fileName ?: return emptyList()

        return loadQuestions(context, fileName)
            .shuffled()
            .take(Constants.QUESTIONS_PER_QUIZ)
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