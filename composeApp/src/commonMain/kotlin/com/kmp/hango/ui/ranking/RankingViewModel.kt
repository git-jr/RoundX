package com.kmp.hango.ui.ranking

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.hango.ScreenshotManager
import com.kmp.hango.network.firebase.UserFireStore
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RankingViewModel(
    private val screenshotManager: ScreenshotManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(RankingState())
    val uiState: StateFlow<RankingState> = _uiState.asStateFlow()

    private var auth: FirebaseAuth = Firebase.auth
    private var userFirestore = UserFireStore()

    init {
        checkLogin()
    }

    private fun checkLogin() {
        _uiState.value = _uiState.value.copy(
            load = true,
            textMessage = "Carregando"
        )

        if (auth.currentUser == null) {
            _uiState.value = _uiState.value.copy(
                load = false,
                textMessage = "É necessário estar logado para acessar o ranking e compartilhar resultados"
            )
        } else {
            loadingProfiles()
        }
    }

    private fun loadingProfiles() {
        viewModelScope.launch {
            userFirestore.getUsersOrderedByScore(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        profiles = it,
                        load = false,
                        textMessage = if (it.isEmpty()) "Sem resultados" else ""
                    )
                    setupCurrentUserPositionOnRanking()
                },
                onError = {
                    _uiState.value = _uiState.value.copy(
                        load = false,
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
                _uiState.value.profiles.indexOfFirst { it.id == userId } + 1
            val rankingMessage =
                "Sua posição atual é: ${if (currentUserPositionOnRanking > 0) "${currentUserPositionOnRanking}º" else "Você ainda não pontuou"}"
            val shareMessage = "Já sou Top ${currentUserPositionOnRanking} no Round X!"
            val currentUserPhotoUrl =
                _uiState.value.profiles.firstOrNull { it.id == userId }?.imageProfileUrl ?: ""

            _uiState.value = _uiState.value.copy(
                rankingMessage = rankingMessage,
                shareMessage = shareMessage,
                textMessage = "",
                currentUserPhotoUrl = currentUserPhotoUrl,
                currentUserPositionOnRanking = currentUserPositionOnRanking
            )
        }
    }

    fun shareRanking(bitmap: ImageBitmap) {
        screenshotManager.shareImage(bitmap)
        _uiState.value = _uiState.value.copy(
            showRankingShare = false
        )
    }

    fun showSnackBar(show: Boolean) {
        _uiState.value = _uiState.value.copy(
            showSnackBar = show
        )
    }

    fun prepareShareRanking() {
        _uiState.value = _uiState.value.copy(
            showRankingShare = true
        )
    }

}