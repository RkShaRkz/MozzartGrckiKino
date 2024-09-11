package repository

import koinModules.ApiClient
import koinModules.`interface`.AvailableGamesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import networking.Fetch20Result
import kotlin.jvm.Synchronized

class AvailableGamesRepositoryImpl(val apiClient: ApiClient): AvailableGamesRepository {
    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    private val listOfGames = mutableListOf<Fetch20Result>()

    @Synchronized
    override fun getAvailableGamesFromNetwork(): List<Fetch20Result> {
        // I want this to be a blocking call and not a suspend method to be blocked on the calling side so...
        return runBlocking {
            coroutineScope.async {
                val next20gamesJsonArray = apiClient.fetchNext20Games()
                val values = remapJsonArrayToDomainObjects(next20gamesJsonArray)

                // Set our list of games to the fetched results
                listOfGames.clear()
                listOfGames.addAll(values)

                //return values
                values
            }.await()
        }
    }

    override fun getAvailableGames(): List<Fetch20Result> {
        return listOfGames
    }

    fun remapJsonArrayToDomainObjects(stringJsonArray: String): List<Fetch20Result> {
        return apiClient.json.decodeFromString<List<Fetch20Result>>(stringJsonArray)
    }
}
