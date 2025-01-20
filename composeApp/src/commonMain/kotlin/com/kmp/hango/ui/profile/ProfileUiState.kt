package com.kmp.hango.ui.profile

data class ProfileUiState(
    val load: Boolean = true,
    val showDeleteDialog: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val showPreferencesDialog: Boolean = false,
    val error: String? = null,
    val goToLogin: Boolean = false,
    val showGoToLogin: Boolean = false,
    val imageProfileUrl: String = "",
    val name: String = "",
    val email: String = "",
    val score: Int = 0,
    val skipCountdown: Boolean = false
)