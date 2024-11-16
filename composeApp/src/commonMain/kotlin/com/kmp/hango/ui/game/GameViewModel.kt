package com.kmp.hango.ui.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.kmp.hango.navigation.Routes
import com.kmp.hango.respository.categorySample
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    var uiState = _uiState.asStateFlow()

    init {
        val categoryId: String = savedStateHandle.toRoute<Routes.Game>().categoryId
        categorySample.find { it.id == categoryId }?.let {
            _uiState.value = _uiState.value.copy(category = it)
        }
    }
}