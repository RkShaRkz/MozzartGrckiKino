package repository

import koinModules.ApiClient
import koinModules.`interface`.TableBetsRepository
import kotlin.jvm.Synchronized

class TableBetsRepositoryImpl(val apiClient: ApiClient): TableBetsRepository {
    val tableBetsMap: HashMap<Int, Set<Int>> = hashMapOf()

    @Synchronized
    override fun getPlayedNumbersForTableId(tableId: Int): Set<Int> {
        //TODO consider throwing here instead of returning an empty set
        return tableBetsMap.get(tableId) ?: setOf()
    }

    @Synchronized
    override fun putPlayedNumbersForTableId(tableId: Int, playedNumbers: Set<Int>) {
        tableBetsMap.put(tableId, playedNumbers)
    }

    override fun fetchTableResultsForTableId(tableId: Int) {
        TODO("Not yet implemented")
    }
}
