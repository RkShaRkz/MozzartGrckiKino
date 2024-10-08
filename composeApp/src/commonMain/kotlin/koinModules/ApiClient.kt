package koinModules

import io.ktor.client.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.plugins.kotlinx.serializer.KotlinxSerializer
import io.ktor.client.statement.bodyAsText
import date.BaseDate
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import networking.DummyValue
import networking.Fetch20Result
import networking.FetchResultsResult
import networking.ResultsItem
import networking.PricePointAddon
import networking.PricePoints
import networking.PrizeCategory
import networking.Sidebets
import networking.SortItem
import networking.WagerStatistics
import networking.WinningNumbers

class ApiClient {
    private val client = HttpClient {
        install(Logging) {
            level = LogLevel.BODY
        }
        install(SERIALIZER_KEY) {
            KotlinxSerializer() // Uses Kotlinx Serialization
        }
    }

    val json = Json {
        serializersModule = SerializersModule {
            // fetch 20 games
            Fetch20Result.serializer()
            PricePoints.serializer()
            PricePointAddon.serializer()
            PrizeCategory.serializer()
            WagerStatistics.serializer()
            DummyValue.serializer()
            // Fetch results
            FetchResultsResult.serializer()
            SortItem.serializer()
            ResultsItem.serializer()
            WinningNumbers.serializer()
            Sidebets.serializer()
        }
        ignoreUnknownKeys = false
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

    suspend fun fetchResults(fromDate: BaseDate, toDate: BaseDate): String {
        val pattern = "yyyy-MM-dd"
        val fromDateString = fromDate.format(pattern)
        val toDateString = toDate.format(pattern)
        return fetchData("https://api.opap.gr/draws/v3.0/${GREEK_KINO_GAME_ID}/draw-date/${fromDateString}/${toDateString}")
    }

    companion object {
        const val GREEK_KINO_GAME_ID = 1100
        const val SERIALIZER_KEY = "serializer"
    }
}
