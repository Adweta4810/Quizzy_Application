package com.dma.studentapplication.utils

import android.content.Context
import com.dma.studentapplication.model.Question
import com.dma.studentapplication.model.QuestionList
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

/**
 * Reads a JSON file from assets/ and returns a shuffled list of [Question].
 *
 * Handles two formats:
 *  - Top-level array:   [ { "questionText": ..., ... }, ... ]
 *  - Wrapped object:    { "questions": [ ... ] }
 */
object JsonLoader {

    private val json = Json { ignoreUnknownKeys = true }

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
}
