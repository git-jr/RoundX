package com.kmp.hango.ui.categoryDetail

import com.kmp.hango.model.Category

data class CategoryDetailUiState(
    val category: Category? = null,
    val textButton: String = "Iniciar",
    val goToGame: Boolean = false,
    val skipCountdown: Boolean = false
)