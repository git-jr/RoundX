package com.kmp.hango.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    var uiState = _uiState.asStateFlow()

    fun showDeleteDialog(showDeleteDialog: Boolean) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(showDeleteDialog = showDeleteDialog)
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            showDeleteDialog(false)
            delay(1000)
            try {
                Firebase.auth.currentUser?.delete()
                _uiState.value = _uiState.value.copy(goToLogin = true)
            } catch (e: Exception) {
                Firebase.auth.signOut()
                _uiState.value = _uiState.value.copy(goToLogin = true)
            }
        }
    }
}