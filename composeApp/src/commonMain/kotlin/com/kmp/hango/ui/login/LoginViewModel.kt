package com.kmp.hango.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.hango.data.QuestionService
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val questionService: QuestionService
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    var uiState = _uiState.asStateFlow()
    private val firebaseAuth: FirebaseAuth = Firebase.auth

    init {
        downloadQuestions()
    }

    private fun downloadQuestions() {
        viewModelScope.launch {

            try {
                val questions = questionService.getAll()
                _uiState.value = uiState.value.copy(questions = questions.map { it.toQuestion() })
                println("questionsResponse success: $questions")
            } catch (e: Exception) {
                println("questionsResponse: error $e")
                _uiState.value = uiState.value.copy(questions = emptyList())
            }
        }
    }

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
                    _uiState.value = uiState.value.copy(
                        goToInit = true,
                        loginError = false,
                    )
                }
            } catch (e: Exception) {
                _uiState.value = uiState.value.copy(loginError = true)
            }
        }
    }
}