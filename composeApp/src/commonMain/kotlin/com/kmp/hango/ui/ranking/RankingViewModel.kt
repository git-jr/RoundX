package com.kmp.hango.ui.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.hango.model.User
import com.kmp.hango.network.firebase.UserFireStore
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RankingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RankingState())
    val uiState: StateFlow<RankingState> = _uiState.asStateFlow()

    private var auth: FirebaseAuth = Firebase.auth
    private var userFirestore = UserFireStore()

    init {
        loadingProfiles()
    }


    private fun loadingProfiles() {
        viewModelScope.launch {
            userFirestore.getUsersOrderedByScore(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        profiles = it,
                        searching = false
                    )
                    setupCurrentUserPositionOnRanking()
                },
                onError = {
                    _uiState.value = _uiState.value.copy(
                        searching = false,
                        textMessage = it
                    )
                }
            )
        }
    }


    private fun setupCurrentUserPositionOnRanking() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            val currentUserPositionOnRanking =
                _uiState.value.profiles.indexOfFirst { it.id == userId }
            _uiState.value = _uiState.value.copy(
                currentUserPositionOnRanking = currentUserPositionOnRanking + 1
            )
        }
    }

}


data class RankingState(
    val profiles: List<User> = listOf(),
    val searching: Boolean = true,
    val textMessage: String = "Carregando",
    val currentUserPositionOnRanking: Int = 0,
)