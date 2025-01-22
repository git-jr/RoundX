package com.kmp.hango.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Question(
    @PrimaryKey
    val id: String,
    val categoryId: String,
    val content: String,
    val image: String,
    val correct: Boolean
)