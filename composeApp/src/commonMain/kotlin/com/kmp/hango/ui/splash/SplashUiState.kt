package com.kmp.hango.ui.splash

import com.kmp.hango.model.Question

data class SplashUiState(
    val loading: Boolean = true,
    val error: Boolean = false,
    val goToInit: Boolean = false,
    val goToLogin: Boolean = false,
    val questions: List<Question> = emptyList()
)