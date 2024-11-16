package com.kmp.hango.ui.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.kmp.hango.navigation.Routes
import com.kmp.hango.respository.questionSamples
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    var uiState = _uiState.asStateFlow()

    init {
        val categoryId: String = savedStateHandle.toRoute<Routes.Game>().categoryId

        questionSamples.filter { it.categoryId == categoryId }.let { questions ->
            val questionsShuffled = questions.shuffled()
            _uiState.value = _uiState.value.copy(
                currentQuestion = questionsShuffled.first(),
                questions = questionsShuffled,
                currentQuestionIndex = 0,
                currentCount = "1/${questionsShuffled.size}",
                answers = List(questionsShuffled.size) { null }
            )
        }
    }

    fun answerQuestion(answer: Boolean) {
        val currentQuestionIndex = uiState.value.currentQuestionIndex
        val currentAnswers = uiState.value.answers.toMutableList()
        currentAnswers[currentQuestionIndex] = answer
        _uiState.value = uiState.value.copy(answers = currentAnswers)
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
                    currentCount = "$answersRemaining/${answers.size}"
                )
            }
        }
    }

    private fun endGame() {
        _uiState.value = uiState.value.copy(
            isGameFinished = true
        )
    }

}