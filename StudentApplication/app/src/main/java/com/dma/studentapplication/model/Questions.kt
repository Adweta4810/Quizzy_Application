package com.dma.studentapplication.model

import kotlinx.serialization.Serializable
import com.dma.studentapplication.utils.*
import com.dma.studentapplication.data.local.room.*
import com.dma.studentapplication.ui.components.*

// ── JSON models ───────────────────────────────────────────────────────────────
// These models map directly to the structure of the JSON asset files in /assets.

/**
 * A single question as parsed from a topic's JSON asset file.
 *
 * Annotated with [@Serializable] so kotlinx. Serialization can decode it
 * directly from the raw JSON without a manual parser.
 *
 * @param questionText       The question prompt shown to the user.
 * @param options            List of answer choices in their original (un-shuffled) order.
 * @param correctAnswerIndex Zero-based index into [options] pointing to the correct answer.
 */
@Serializable
data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

/**
 * Wrapper object used by JSON files that nest their questions under a
 * `"questions"` key (all topics except math.json which is a bare array).
 *
 * [JsonLoader] handles both formats automatically.
 *
 * @param questions The list of questions contained in the file.
 */
@Serializable
data class QuestionList(
    val questions: List<Question>
)

// ── Domain models ─────────────────────────────────────────────────────────────

/**
 * Metadata for a single quiz topic sourced from [constants.quizTopics].
 *
 * Acts as the single source of truth for topic id → display name → asset file
 * mapping. [JsonLoader] resolves the filename from this model.
 *
 * @param id           Snake_case identifier used in navigation routes (e.g. "current_affairs").
 * @param title        Human-readable display name (e.g. "Current Affairs").
 * @param fileName     Asset filename relative to the assets root (e.g. "current_affairs.json").
 * @param iconEmoji    Emoji used as the topic icon in some UI contexts.
 * @param bgColorHex   Pastel hex background color for the topic card (e.g. "#FEF3C7").
 * @param iconColorHex Accent hex color applied to the topic icon (e.g. "#F97316").
 */
data class QuizTopic(
    val id: String,
    val title: String,
    val fileName: String,
    val iconEmoji: String,
    val bgColorHex: String,
    val iconColorHex: String
)

/**
 * Records the user's response to a single question during an active quiz session.
 *
 * @param question      The original [Question] that was presented.
 * @param selectedIndex Zero-based index of the option the user chose.
 *                      -1 indicates the question was skipped or the timer expired.
 * @param timeTaken     Seconds the user spent on this question before locking their answer.
 */
data class AnsweredQuestion(
    val question: Question,
    val selectedIndex: Int,
    val timeTaken: Int
) {
    /** True if the user's selection matches the correct answer index. */
    val isCorrect: Boolean get() = selectedIndex == question.correctAnswerIndex

    /** True if the question was skipped or the timer ran out before an answer was locked. */
    val wasSkipped: Boolean get() = selectedIndex == -1
}

/**
 * Transient summary of a completed quiz session passed between the Quiz,
 * Result, and Review screens.
 *
 * This is an in-memory model — it is not persisted directly. The repository
 * converts it into a [QuizResultEntity] for Room storage.
 *
 * @param topicId          Snake_case topic identifier.
 * @param topicTitle       Human-readable topic name shown on result screens.
 * @param score            Number of correct answers.
 * @param totalQuestions   Total questions in the session.
 * @param xpEarned         XP calculated from correct answers and any perfect-score bonus.
 * @param timeTakenSeconds Total elapsed time for the quiz in seconds.
 * @param answers          Ordered list of per-question responses.
 */
data class QuizResult(
    val topicId: String,
    val topicTitle: String,
    val score: Int,
    val totalQuestions: Int,
    val xpEarned: Int,
    val timeTakenSeconds: Int,
    val answers: List<AnsweredQuestion>
) {
    /**
     * Score expressed as a percentage (0–100).
     * Returns 0 if [totalQuestions] is 0 to avoid division by zero.
     */
    val accuracyPercent: Int
        get() = if (totalQuestions == 0) 0 else (score * 100) / totalQuestions
}

// ── Mascot mood enum ──────────────────────────────────────────────────────────

/**
 * Emotional states for the RoboBuddy mascot.
 *
 * The active state is passed to [RoboBuddy] composable which renders the
 * corresponding illustration. States are chosen by the quiz flow based on
 * the user's performance and the current timer value.
 *
 * Usage examples:
 * - [CORRECT]   — user answered correctly
 * - [INCORRECT] — user answered wrongly
 * - [SURPRISED] — timer is critically low
 * - [SAD]       — timed out with no answer
 * - [WAVE]      — splash screen greeting
 * - [TROPHY]    — perfect score celebration
 */
enum class RoboBuddyState {
    WAVE, CELEBRATE, THUMBS_UP, READING, THINKING,
    HAPPY, EXCITED, LOVE, SURPRISED, SAD,
    ANGRY, LAUGHING, WINK,
    CORRECT, INCORRECT, STUDYING, WRITING,
    TROPHY, IDEA, PROGRESS
}