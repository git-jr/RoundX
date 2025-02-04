package com.kmp.hango.ui.game

import com.kmp.hango.constant.DEFAULT_BG_COLOR
import com.kmp.hango.model.Question

data class GameUiState(
    val currentQuestion: Question? = null,
    val questions: List<Question> = emptyList(),
    val answers: List<Boolean?> = emptyList(),
    val correctAnswers: List<Boolean> = emptyList(),
    val categoryColor: Long = DEFAULT_BG_COLOR,
    val currentQuestionIndex: Int = 0,
    val currentCount: String = "1/5",
    val isGameFinished: Boolean = false,
    val score: Int = 0,
    val result: String = "",
    val currentTime: Int = 0,
    val messageText: String = "",
    val syncMessage: String = "Sincronize seu progresso para competir com outros jogadores!",
    val goToRanking: Boolean = false,
)