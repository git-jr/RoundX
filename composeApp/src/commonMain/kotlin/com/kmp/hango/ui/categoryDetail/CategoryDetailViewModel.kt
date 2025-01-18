package com.kmp.hango.ui.categoryDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.hango.respository.categorySample
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryDetailUiState())
    var uiState = _uiState.asStateFlow()

    private val listShapes = listOf(
        "▲ ▲ 3 ▲ ▲",
        "■ ■ 2 ■ ■",
        "● ● 1 ● ●",
        "✖ ✖ VAI ✖ ✖"
    )

    fun prepareScreen(categoryId: String) {
        categorySample.find { it.id == categoryId }?.let {
            _uiState.value = _uiState.value.copy(category = it)
        }
    }

    fun startGame() {
        viewModelScope.launch {
            listShapes.forEach {
                _uiState.value = _uiState.value.copy(
                    textButton = it
                )
            }
            _uiState.value = _uiState.value.copy(
                goToGame = true
            )
            delay(100)
            _uiState.value = _uiState.value.copy(
                goToGame = false
            )
        }
    }
}