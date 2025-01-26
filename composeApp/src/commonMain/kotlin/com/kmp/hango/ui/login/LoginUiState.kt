package com.kmp.hango.ui.login

import com.kmp.hango.model.Question

data class LoginUiState(
    val userName: String = "",
    val password: String = "",
    val loginError: Boolean = false,
    val goToRegister: Boolean = false,
    val goToInit: Boolean = false,
    val questions: List<Question> = emptyList()
)