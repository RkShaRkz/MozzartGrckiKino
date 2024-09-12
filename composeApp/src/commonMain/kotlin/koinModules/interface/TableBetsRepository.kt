package koinModules.`interface`

import networking.FetchResultsResult
import networking.ResultsItem

/**
 * Repository that holds all bets (played numbers) for a particular table (drawId)
 */
interface TableBetsRepository {

    fun getPlayedNumbersForTableId(tableId: Int): Set<Int>

    fun putPlayedNumbersForTableId(tableId: Int, playedNumbers: Set<Int>)

    fun getAllResults(): List<ResultsItem>

    fun getTableResultsForTableId(tableId: Int): ResultsItem?

    fun fetchResultsFromNetwork(): FetchResultsResult
}
