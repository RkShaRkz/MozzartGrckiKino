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

    companion object {
        const val SERIALIZER_KEY = "serializer"
    }
}
