package com.kmp.hango.ui.categoryDetail

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.hango.respository.categorySample
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CategoryDetailViewModel(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryDetailUiState())
    var uiState = _uiState.asStateFlow()

    init {
        loadPreferences()
    }
    private fun loadPreferences() {
        viewModelScope.launch {
            val skipCountdown = dataStore.data.first()[booleanPreferencesKey("skipCountdown")] ?: false
            _uiState.value = _uiState.value.copy(skipCountdown = skipCountdown)
        }
    }


    fun prepareScreen(categoryId: String) {
        categorySample.find { it.id == categoryId }?.let {
            _uiState.value = _uiState.value.copy(category = it)
        }
    }

    fun startGame() {
        if(uiState.value.skipCountdown) {
            _uiState.value = _uiState.value.copy(goToGame = true)
            return
        }

        val listShapes = listOf(
            "▲ ▲ 3 ▲ ▲",
            "■ ■ 2 ■ ■",
            "● ● 1 ● ●",
            "✖ ✖ VAI ✖ ✖"
        )

        viewModelScope.launch {
            listShapes.forEach {
                _uiState.value = _uiState.value.copy(textButton = it)
                delay(500)
            }
            _uiState.value = _uiState.value.copy(goToGame = true)
            delay(100)
            _uiState.value = _uiState.value.copy(goToGame = false)
        }
    }
}