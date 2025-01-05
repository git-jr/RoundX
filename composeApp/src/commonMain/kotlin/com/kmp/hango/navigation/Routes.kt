package com.kmp.hango.navigation

import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    data object Init : Routes()

    @Serializable
    data object Search : Routes()

    @Serializable
    data object Orders : Routes()

    @Serializable
    data object Profile : Routes()

    @Serializable
    data class CategoryDetail(
        val categoryId: String,
        val categoryColor: Long = 0XFF034d58
    ) : Routes()

    @Serializable
    data class Game(val categoryId: String) : Routes()

}