package com.kmp.hango.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val name: String = "",
    val score: Int = 0,
    val imageProfileUrl: String = "",
)