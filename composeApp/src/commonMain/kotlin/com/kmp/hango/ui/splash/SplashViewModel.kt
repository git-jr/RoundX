package com.kmp.hango.ui.splash

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.hango.respository.QuestionRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class SplashViewModel(
    private val questionRepository: QuestionRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    var uiState = _uiState.asStateFlow()
    private val firebaseAuth: FirebaseAuth = Firebase.auth

    init {
        startChecks()
    }

    fun startChecks() {
        viewModelScope.launch {
            val questionsDownloaded =
                dataStore.data.first()[booleanPreferencesKey("questions_downloaded")] ?: false
            if (!questionsDownloaded) {
                downloadQuestions()
            } else {
                checkUserLogged()
            }
        }
    }


    private fun checkUserLogged() {
        _uiState.value = if (firebaseAuth.currentUser != null) {
            uiState.value.copy(goToInit = true)
        } else {
            uiState.value.copy(goToLogin = true)
        }
        _uiState.value = uiState.value.copy(loading = false)
    }


    private fun downloadQuestions() {
        viewModelScope.launch {
            try {
                val questions = questionRepository.getAll()
                _uiState.value = uiState.value.copy(questions = questions)
                saveQuestionsDatabase()
            } catch (e: Exception) {
                _uiState.value = uiState.value.copy(
                    questions = emptyList(),
                    loading = false,
                    error = true
                )
            }
        }
    }

    private fun saveQuestionsDatabase() {
        viewModelScope.launch {
            try {
                dataStore.edit { preferences ->
                    preferences[booleanPreferencesKey("questions_downloaded")] = true
                }

                _uiState.value.questions.let { questions ->
                    questionRepository.saveLocal(questions)
                }

                _uiState.value = uiState.value.copy(loading = false)
                checkUserLogged()
            } catch (e: Exception) {
                _uiState.value = uiState.value.copy(
                    loading = false,
                    error = true
                )
            }
        }
    }
}