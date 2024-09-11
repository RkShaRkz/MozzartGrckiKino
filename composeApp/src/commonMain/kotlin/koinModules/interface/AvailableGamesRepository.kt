package koinModules.`interface`

import networking.Fetch20Result

interface AvailableGamesRepository {

    fun getAvailableGamesFromNetwork(): List<Fetch20Result>

    fun getAvailableGames(): List<Fetch20Result>
}
