package com.kmp.hango.ui.game

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.hango.ScreenshotManager
import com.kmp.hango.extensions.toTime
import com.kmp.hango.extensions.zeroRound
import com.kmp.hango.model.User
import com.kmp.hango.respository.questionSamples
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GameViewModel(
    private val screenshotManager: ScreenshotManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    var uiState = _uiState.asStateFlow()

    private val firebaseRealtimeDatabase = Firebase.database
    private val dbUsers = firebaseRealtimeDatabase.reference("users")


    private fun initTimer() {
        viewModelScope.launch {
            while (_uiState.value.isGameFinished.not()) {
                delay(1_000)
                _uiState.value = uiState.value.copy(
                    currentTime = uiState.value.currentTime + 1
                )
            }
        }
    }

    fun answerQuestion(answer: Boolean) {
        val currentQuestionIndex = uiState.value.currentQuestionIndex
        val currentAnswers = uiState.value.answers.toMutableList()
        currentAnswers[currentQuestionIndex] = answer
        _uiState.value = uiState.value.copy(answers = currentAnswers)

        val isCorrect = uiState.value.currentQuestion?.correct == answer

        _uiState.value =
            uiState.value.copy(correctAnswers = uiState.value.correctAnswers + isCorrect)
        goNextQuestion()
    }

    private fun goNextQuestion() {
        with(uiState.value) {
            val currentQuestionIndex = currentQuestionIndex + 1
            if (currentQuestionIndex == questions.size) {
                endGame()
            } else {
                val answersRemaining = answers.filterNotNull().size + 1
                _uiState.value = uiState.value.copy(
                    currentQuestion = questions[currentQuestionIndex],
                    currentQuestionIndex = currentQuestionIndex,
                    currentCount = "${answersRemaining.zeroRound()}/${answers.size.zeroRound()}"
                )
            }
        }
    }

    private fun endGame() {
        with(uiState.value) {
            val score = correctAnswers.count { it }
            val scoreFormatted = "${score.zeroRound()}/${questions.size.zeroRound()}"
            val timerFormatted = currentTime.toTime()
            val currentScore = calculateScore(score, currentTime)

            _uiState.value = uiState.value.copy(
                isGameFinished = true,
                score = currentScore,
                result = "$scoreFormatted - $timerFormatted",
                messageText = "Você ganhou ${currentScore} pontos!"
            )
        }
    }

    fun prepareScreen(categoryId: String) {
        questionSamples.filter { it.categoryId == categoryId }.let { questions ->
            val questionsShuffled = questions.shuffled()
            _uiState.value = _uiState.value.copy(
                currentQuestion = questionsShuffled.first(),
                questions = questionsShuffled,
                currentQuestionIndex = 0,
                currentCount = "01/${questionsShuffled.size.zeroRound()}",
                answers = List(questionsShuffled.size) { null }
            )
        }

        initTimer()
    }

    private fun calculateScore(correctAnswers: Int, time: Int): Int {
        val basePoints = correctAnswers * 5
        val totalTimeAvailable = 100
        val timeRest = ((totalTimeAvailable - time) * 0.5).coerceAtLeast(0.0)
        return (basePoints + timeRest).toInt()
    }

    fun synchronizeProgress() {
        if (Firebase.auth.currentUser == null) {
            _uiState.value = uiState.value.copy(
                syncMessage = "Você precisa estar logado para sincronizar seu progresso"
            )
        } else {
            _uiState.value = uiState.value.copy(
                syncMessage = "Sincronizando..."
            )
            try {
                val userId = Firebase.auth.currentUser?.uid.toString()
                val userRef = dbUsers.child(userId)
                viewModelScope.launch {
                    val userSnapshot = userRef.valueEvents.first()
                    val user = userSnapshot.value<User>()
                    user.let {
                        val lastScore = user.score
                        val currentScore = uiState.value.score
                        val newScore = currentScore + lastScore
                        userRef.updateChildren(hashMapOf("score" to newScore))
                    }
                    _uiState.value = uiState.value.copy(
                        syncMessage = "Sincronizado com sucesso!"
                    )
                    delay(500)
                    _uiState.value = uiState.value.copy(
                        goToRanking = true
                    )
                }
            } catch (e: Exception) {
                _uiState.value = uiState.value.copy(
                    syncMessage = "Erro ao sincronizar progresso: ${e.message}"
                )
            }
        }
    }
}