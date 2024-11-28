package com.kmp.hango.ui.categoryDetail

import androidx.lifecycle.ViewModel
import com.kmp.hango.respository.categorySample
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CategoryDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryDetailUiState())
    var uiState = _uiState.asStateFlow()

    fun prepareScreen(categoryId: String) {
        categorySample.find { it.id == categoryId }?.let {
            _uiState.value = _uiState.value.copy(category = it)
        }
    }
}