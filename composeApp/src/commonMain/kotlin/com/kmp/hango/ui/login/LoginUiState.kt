package com.kmp.hango.ui.login

data class LoginUiState(
    val userName: String = "",
    val password: String = "",
    val loginError: Boolean = false
)