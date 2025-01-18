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
        checkLogin()
    }

    private fun checkLogin() {
        _uiState.value = _uiState.value.copy(
            searching = true,
            textMessage = "Carregando"
        )

        if (auth.currentUser == null) {
            _uiState.value = _uiState.value.copy(
                searching = false,
                textMessage = "É necessário estar logado para acessar o ranking"
            )
        } else {
            loadingProfiles()
//            loadingProfilesMock()
        }
    }

    private fun loadingProfiles() {
        viewModelScope.launch {
            userFirestore.getUsersOrderedByScore(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        profiles = it,
                        searching = false,
                        textMessage = if (it.isEmpty()) "Sem resultados" else ""
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

    private fun loadingProfilesMock() {
        viewModelScope.launch {
            val profilesTest = listOf(
                User(
                    id = "wYOphEmhyUeYRjTVqHaDHSU9Abk2",
                    name = "Paradoxo",
                    imageProfileUrl = "https://m.media-amazon.com/images/S/pv-target-images/311b1a944798a6352dee106783f8b1c276b1fe7446c9dd4bb1898bdf50565cd7.jpg",
                    score = 456,
                ),
                User(
                    id = "1",
                    name = "Nilo",
                    imageProfileUrl = "https://m.media-amazon.com/images/S/pv-target-images/311b1a944798a6352dee106783f8b1c276b1fe7446c9dd4bb1898bdf50565cd7.jpg",
                    score = 456,

                    ),
                User(
                    id = "2",
                    name = "Rubens",
                    imageProfileUrl = "https://m.media-amazon.com/images/S/pv-target-images/311b1a944798a6352dee106783f8b1c276b1fe7446c9dd4bb1898bdf50565cd7.jpg",
                    score = 456,

                    ),
                User(
                    id = "3",
                    name = "Elis",
                    imageProfileUrl = "adasd",
                    score = 33,

                    ),
                User(
                    id = "4",
                    name = "Tania",
                    imageProfileUrl = "aadasddasd",
                    score = 22,
                ),
                User(
                    id = "6",
                    name = "Gerda",
                    imageProfileUrl = "aadasddasd",
                    score = 22,
                ),
                User(
                    id = "7",
                    name = "Lisa",
                    imageProfileUrl = "aadasddasd",
                    score = 22,
                ),
            )

            _uiState.value = _uiState.value.copy(
                profiles = profilesTest,
                searching = false,
                textMessage = ""
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

            _uiState.value = _uiState.value.copy(
                rankingMessage = rankingMessage
            )
        }
    }

}