package com.kmp.hango.ui.ranking

import com.kmp.hango.model.User


data class RankingState(
    val profiles: List<User> = listOf(),
    val searching: Boolean = true,
    val textMessage: String = "Carregando",
    val rankingMessage: String = ""
)