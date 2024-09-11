package koinModules.`interface`

import networking.Fetch20Result

/**
 * Repository class holding information about fetched/available games, as well as being able to fetch
 * games from the network
 */
interface AvailableGamesRepository {
    /**
     * Fetch games from the network and refresh internal game list
     */
    fun getAvailableGamesFromNetwork(): List<Fetch20Result>

    /**
     * Just return internal game list
     */
    fun getAvailableGames(): List<Fetch20Result>
}
