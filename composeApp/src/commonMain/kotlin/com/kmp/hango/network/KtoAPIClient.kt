package com.kmp.hango.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal object KtoAPIClient {
    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }


        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        defaultRequest {
            accept(ContentType.Any)
            url(
                urlString = "https://raw.githubusercontent.com/git-jr/sample-files/refs/heads/main/api/"
            )
        }
    }
}