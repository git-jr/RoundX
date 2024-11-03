package com.kmp.hango.navigation

import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    data object MainLis : Routes()

    @Serializable
    data object Search : Routes()

    @Serializable
    data object Orders : Routes()

    @Serializable
    data object Profile : Routes()

    @Serializable
    data class ProductDetail(val productId: String? = null) : Routes()

}