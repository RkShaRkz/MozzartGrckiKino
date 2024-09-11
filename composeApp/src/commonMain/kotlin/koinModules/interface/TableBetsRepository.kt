package koinModules.`interface`

/**
 * Repository that holds all bets (played numbers) for a particular table (drawId)
 */
interface TableBetsRepository {

    fun getPlayedNumbersForTableId(tableId: Int): Set<Int>

    fun putPlayedNumbersForTableId(tableId: Int, playedNumbers: Set<Int>)

    fun fetchTableResultsForTableId(tableId: Int)
}
