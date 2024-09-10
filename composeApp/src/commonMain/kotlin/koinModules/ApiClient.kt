package koinModules

import io.ktor.client.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.plugins.kotlinx.serializer.KotlinxSerializer
import io.ktor.client.statement.bodyAsText

class ApiClient {
    private val client = HttpClient {
        install(Logging) {
            level = LogLevel.BODY
        }
        install(SERIALIZER_KEY) {
            KotlinxSerializer() // Uses Kotlinx Serialization
        }
    }

    suspend fun fetchData(url: String): String {
        return client.get(url).bodyAsText()
    }

    fun close() {
        client.close()
    }

    suspend fun fetchNext20Games(): String {
        return fetchData("https://api.opap.gr/draws/v3.0/${GREEK_KINO_GAME_ID}/upcoming/20")
    }

    suspend fun fetchDrawByID(drawId: String): String {
        return fetchData("https://api.opap.gr/draws/v3.0/${GREEK_KINO_GAME_ID}/${drawId}")
    }

    companion object {
        const val GREEK_KINO_GAME_ID = 1100
        const val SERIALIZER_KEY = "serializer"
    }
}
