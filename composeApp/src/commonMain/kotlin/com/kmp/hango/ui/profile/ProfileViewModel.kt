package com.kmp.hango.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.hango.network.firebase.UserFireStore
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(

) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    var uiState = _uiState.asStateFlow()

    private val firebaseAuth: FirebaseAuth = Firebase.auth
    private var userFirestore = UserFireStore()

    init {
        checkLogin()
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
}