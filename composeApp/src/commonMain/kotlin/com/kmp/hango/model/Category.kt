package com.kmp.hango.model

import org.jetbrains.compose.resources.DrawableResource

data class Category(
    val id: String,
    val title: String,
    val description: String,
    val color: Long,
    val icon: DrawableResource
)