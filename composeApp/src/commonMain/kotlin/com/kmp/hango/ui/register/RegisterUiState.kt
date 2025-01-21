package com.kmp.hango.ui.register

import io.github.vinceglb.filekit.core.PlatformFile

data class RegisterUiState(
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val load: Boolean = false,
    val loginError: Boolean = false,
    val goToRegister: Boolean = false,
    val goToInit: Boolean = false,
    val imageProfile: PlatformFile? = null,
    val imageByteArray: ByteArray? = null,
    val loginErrorMessage: String = "",
    val loginErrorMessageDetail: String = "",
)