package com.kmp.hango.data

import com.kmp.hango.model.QuestionResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType.Application.Json
import kotlinx.serialization.json.Json

interface QuestionService {
    suspend fun getAll(): List<QuestionResponse>
}

class QuestionServiceImpl(
    private val httpClient: HttpClient
) : QuestionService {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getAll(): List<QuestionResponse> {
        return runCatching {
            val bodyText = httpClient.get("questions.json").body<String>()
            json.decodeFromString<List<QuestionResponse>>(bodyText)
        }.getOrElse { e ->
            println("Erro ao processar questions.json: $e")
            emptyList()
        }
    }
}