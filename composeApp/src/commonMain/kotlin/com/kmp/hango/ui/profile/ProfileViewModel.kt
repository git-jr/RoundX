package com.kmp.hango.ui.profile

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kmp.hango.data.database.QuestionDao
import com.kmp.hango.network.firebase.UserFireStore
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val dataStore: DataStore<Preferences>,
    private val dao: QuestionDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    var uiState = _uiState.asStateFlow()

    private val firebaseAuth: FirebaseAuth = Firebase.auth
    private var userFirestore = UserFireStore()

    init {
        viewModelScope.launch {
//            dao.insert(
//                com.kmp.hango.model.Question(
//                    id = "1",
//                    categoryId = "1",
//                    content = "content",
//                    image = "image",
//                    correct = true
//                )
//            )

//            dao.getAll().collect {
//                println("Room funcionando: $it")
//            }
        }
        checkLogin()
        loadPreferences()
    }

    private fun checkLogin() {
        val user = firebaseAuth.currentUser
        user?.let {
            _uiState.value = _uiState.value.copy(email = user.email.toString())
            loadInfos(user.uid)
        } ?: run {
            _uiState.value = _uiState.value.copy(showGoToLogin = true, load = false)
        }
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            val skipCountdown = dataStore.data.first()[booleanPreferencesKey("skipCountdown")] ?: false
            _uiState.value = _uiState.value.copy(skipCountdown = skipCountdown)
        }
    }

    private fun loadInfos(userId: String) {
        viewModelScope.launch {
            userFirestore.getUserById(
                userId,
                onSuccess = { user ->
                    _uiState.value = _uiState.value.copy(
                        name = user.name,
                        imageProfileUrl = user.imageProfileUrl,
                        score = user.score,
                        load = false
                    )
                },
                onError = {
                    _uiState.value = _uiState.value.copy(showGoToLogin = true, load = false)
                }
            )
        }
    }

    fun showDeleteDialog(showDeleteDialog: Boolean) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(showDeleteDialog = showDeleteDialog)
        }
    }

    fun showLogoutDialog(showLogoutDialog: Boolean) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(showLogoutDialog = showLogoutDialog)
        }
    }


    fun showPreferencesDialog(show: Boolean) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(showPreferencesDialog = show)
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            showDeleteDialog(false)
            delay(1000)
            try {
                firebaseAuth.currentUser?.delete()
                _uiState.value = _uiState.value.copy(goToLogin = true)
            } catch (e: Exception) {
                Firebase.auth.signOut()
                _uiState.value = _uiState.value.copy(goToLogin = true)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            firebaseAuth.signOut()
            _uiState.value = _uiState.value.copy(goToLogin = true)
        }
    }

    fun setSkipCountdown(skip: Boolean) {
        viewModelScope.launch {
            dataStore.edit {
                it[booleanPreferencesKey("skipCountdown")] = skip
            }
            _uiState.value = _uiState.value.copy(skipCountdown = skip)
        }
    }
}