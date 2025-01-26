package com.kmp.hango.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
data class Question(
    @PrimaryKey
    val id: String,
    val categoryId: String,
    val content: String,
    val image: String,
    val correct: Boolean
)

@Serializable
data class QuestionResponse(
    val id: String,
    val categoryId: String,
    val content: String,
    val image: String,
    val correct: Boolean
) {
    fun toQuestion(): Question {
        return Question(
            id = id,
            categoryId = categoryId,
            content = content,
            image = image,
            correct = correct
        )
    }
}