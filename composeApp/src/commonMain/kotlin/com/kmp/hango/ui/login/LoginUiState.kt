package com.kmp.hango.ui.login

data class LoginUiState(
    val userName: String = "email@teste.com",
    val password: String = "senha123",
    val loginError: Boolean = false
)