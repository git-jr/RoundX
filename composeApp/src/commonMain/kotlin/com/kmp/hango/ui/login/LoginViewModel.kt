package com.kmp.hango.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    var uiState = _uiState.asStateFlow()
    val firebaseAuth: FirebaseAuth = Firebase.auth

    fun updateUsername(value: String) {
        _uiState.value = uiState.value.copy(userName = value)
    }

    fun updatePassword(value: String) {
        _uiState.value = uiState.value.copy(password = value)
    }

    fun login() {
        val username = uiState.value.userName
        val password = uiState.value.password


        viewModelScope.launch {
           try {
                val authResult = firebaseAuth.signInWithEmailAndPassword(
                     username,
                     password
                )

                if (authResult.user != null) {
                     _uiState.value = uiState.value.copy(loginError = false)
                }
              } catch (e: Exception) {
                _uiState.value = uiState.value.copy(loginError = true)
           }
        }
    }
}