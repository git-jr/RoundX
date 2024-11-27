package com.kmp.hango.ui.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kmp.hango.extensions.toTime
import com.kmp.hango.extensions.zeroRound
import com.kmp.hango.navigation.Routes
import com.kmp.hango.respository.questionSamples
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class GameViewModel(
//    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    var uiState = _uiState.asStateFlow()

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

        if (uiState.value.currentQuestion?.correct == answer) {
            _uiState.value = uiState.value.copy(score = uiState.value.score + 1)
        }
        foNextQuestion()
    }

    private fun foNextQuestion() {
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
            val score = answers.count { it == true }
            val scoreFormatted = "${score.zeroRound()}/${questions.size.zeroRound()}"
            val timerFormatted = currentTime.toTime()


            _uiState.value = uiState.value.copy(
                isGameFinished = true,
                score = uiState.value.answers.count { it == true },
                result = "$scoreFormatted - $timerFormatted"
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

}