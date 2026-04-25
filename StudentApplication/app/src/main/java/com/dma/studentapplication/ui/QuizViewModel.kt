package com.dma.studentapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dma.studentapplication.data.local.room.QuizResultEntity
import com.dma.studentapplication.repository.QuizRepository
import com.dma.studentapplication.ui.model.QuizQuestionUi
import com.dma.studentapplication.ui.screens.ReviewQuestionItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {

    // ── Active quiz questions ─────────────────────────────────────────────────

    private val _questions = MutableStateFlow<List<QuizQuestionUi>>(emptyList())
    val questions: StateFlow<List<QuizQuestionUi>> = _questions

    /**
     * Loads 10 shuffled questions for [topicId] on a background thread.
     * Called when the Quiz composable first enters composition.
     */
    fun loadQuestions(topicId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _questions.value = repository.getQuestions(topicId)
        }
    }

    /** Clears questions so stale data is not shown when a new quiz starts. */
    fun clearQuestions() {
        _questions.value = emptyList()
    }

    // ── Save result after quiz completion ─────────────────────────────────────

    /**
     * Row id of the most recently saved result.
     * ResultScreen collects this to navigate to ReviewScreen with the correct id.
     */
    private val _savedResultId = MutableStateFlow<Long?>(null)
    val savedResultId: StateFlow<Long?> = _savedResultId

    /**
     * Serializes [reviewItems] and persists the completed quiz to ROOM.
     * Emits the new row id via [savedResultId].
     */
    fun saveResult(
        topic: String,
        score: Int,
        totalQuestions: Int,
        timeTaken: String,
        reviewItems: List<ReviewQuestionItem>
    ) {
        viewModelScope.launch {
            val id = repository.saveResult(
                topic          = topic,
                score          = score,
                totalQuestions = totalQuestions,
                timeTaken      = timeTaken,
                reviewItems    = reviewItems
            )
            _savedResultId.value = id
        }
    }

    /** Resets [savedResultId] so the old id is not re-used on next quiz. */
    fun clearSavedResultId() {
        _savedResultId.value = null
    }

    // ── History list ──────────────────────────────────────────────────────────

    /**
     * Live stream of all saved results newest-first.
     * HistoryScreen collects this to build its list.
     */
    val allResults: StateFlow<List<QuizResultEntity>> =
        repository.getAllResults()
            .stateIn(
                scope            = viewModelScope,
                started          = SharingStarted.WhileSubscribed(5_000),
                initialValue     = emptyList()
            )

    // ── Detail / Review ───────────────────────────────────────────────────────

    private val _detailResult = MutableStateFlow<QuizResultEntity?>(null)
    val detailResult: StateFlow<QuizResultEntity?> = _detailResult

    /**
     * Loads a single [QuizResultEntity] by [id] for ReviewScreen or
     * HistoryDetailScreen.
     */
    fun loadDetail(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _detailResult.value = repository.getResultById(id)
        }
    }

    /** Clears cached detail so a stale record is not shown briefly. */
    fun clearDetail() {
        _detailResult.value = null
    }

    // ── Clear all history ─────────────────────────────────────────────────────

    fun clearAllHistory() {
        viewModelScope.launch { repository.clearAll() }
    }
}

// ── Factory ───────────────────────────────────────────────────────────────────

class QuizViewModelFactory(private val repository: QuizRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        QuizViewModel(repository) as T
}