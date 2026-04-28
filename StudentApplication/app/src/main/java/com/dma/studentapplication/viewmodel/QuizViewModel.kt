package com.dma.studentapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dma.studentapplication.data.local.room.QuizResultEntity
import com.dma.studentapplication.repository.QuizRepository
import com.dma.studentapplication.ui.screens.quiz.QuizQuestionUi
import com.dma.studentapplication.ui.screens.review.ReviewQuestionItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {

    // ── Active quiz questions ─────────────────────────────────────────────────

    private val _questions = MutableStateFlow<List<QuizQuestionUi>>(emptyList())
    val questions: StateFlow<List<QuizQuestionUi>> = _questions

    // Tracks which topic is already loaded so rotation never triggers a reload.
    private var activeTopicId: String? = null

    /**
     * Loads and shuffles questions exactly once per quiz session.
     *
     * Subsequent calls with the same [topicId] while questions are already
     * loaded (e.g. after a screen rotation) are intentionally ignored so the
     * question list — including the shuffled option order — stays stable.
     */
    fun loadQuestions(topicId: String) {
        // Guard: skip if this topic is already loaded.
        if (activeTopicId == topicId && _questions.value.isNotEmpty()) return

        activeTopicId = topicId
        fetchAndShuffle(topicId)
    }

    /**
     * Forces a brand-new quiz session (e.g. user taps "Restart").
     * Always fetches fresh questions and re-shuffles, regardless of state.
     */
    fun startNewQuiz(topicId: String) {
        activeTopicId = topicId
        fetchAndShuffle(topicId)
    }

    /**
     * Fetches questions from the repository and shuffles the options of each
     * question once, storing the result in [_questions].
     *
     * Shuffling is done here in the ViewModel so it happens exactly once and
     * survives configuration changes. The repository itself must NOT shuffle —
     * it should return questions with a stable option order every time.
     */
    private fun fetchAndShuffle(topicId: String) {
        viewModelScope.launch {
            val loaded = withContext(Dispatchers.IO) {
                repository.getQuestions(topicId)
            }

            // Shuffle answer options once here so the order is fixed for the
            // entire session.  Each QuizQuestionUi gets a new options list
            // with its correct answer randomised into a different position.
            _questions.value = loaded.map { q ->
                q.copy(options = q.options.shuffled())
            }
        }
    }

    /**
     * Clears the current quiz session.
     * Do NOT call this on screen rotation — only call it when the user
     * deliberately exits or finishes the quiz.
     */
    fun clearQuestions() {
        activeTopicId = null
        _questions.value = emptyList()
    }

    // ── Save result after quiz completion ─────────────────────────────────────

    private val _savedResultId = MutableStateFlow<Long?>(null)
    val savedResultId: StateFlow<Long?> = _savedResultId

    fun saveResult(
        topic: String,
        score: Int,
        totalQuestions: Int,
        timeTaken: String,
        reviewItems: List<ReviewQuestionItem>,
        onSaved: (Long) -> Unit = {}
    ) {
        viewModelScope.launch {
            val id = withContext(Dispatchers.IO) {
                repository.saveResult(
                    topic = topic,
                    score = score,
                    totalQuestions = totalQuestions,
                    timeTaken = timeTaken,
                    reviewItems = reviewItems
                )
            }

            _savedResultId.value = id
            onSaved(id)
        }
    }

    fun clearSavedResultId() {
        _savedResultId.value = null
    }

    // ── History list ──────────────────────────────────────────────────────────

    val allResults: StateFlow<List<QuizResultEntity>> =
        repository.getAllResults()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    // ── Detail / Review ───────────────────────────────────────────────────────

    private val _detailResult = MutableStateFlow<QuizResultEntity?>(null)
    val detailResult: StateFlow<QuizResultEntity?> = _detailResult

    fun loadDetail(id: Long) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getResultById(id)
            }

            _detailResult.value = result
        }
    }

    fun clearDetail() {
        _detailResult.value = null
    }

    // ── Clear all history ─────────────────────────────────────────────────────

    fun clearAllHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAll()
        }
    }
}

// ── Factory ───────────────────────────────────────────────────────────────────

class QuizViewModelFactory(private val repository: QuizRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        QuizViewModel(repository) as T
}