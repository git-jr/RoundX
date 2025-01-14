package com.kmp.hango.ui.register

import io.github.vinceglb.filekit.core.PlatformFile

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val loginError: Boolean = false,
    val goToRegister: Boolean = false,
    val goToInit: Boolean = false,
    val imageProfile: PlatformFile? = null,
    val imageByteArray: ByteArray? = null
)