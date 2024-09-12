package repository

import date.BaseDate
import date.createDate
import koinModules.ApiClient
import koinModules.`interface`.TableBetsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.jvm.Synchronized

class TableBetsRepositoryImpl(val apiClient: ApiClient) : TableBetsRepository {
    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    val tableBetsMap: HashMap<Int, Set<Int>> = hashMapOf()

    @Synchronized
    override fun getPlayedNumbersForTableId(tableId: Int): Set<Int> {
        return tableBetsMap.get(tableId) ?: setOf()
    }

    @Synchronized
    override fun putPlayedNumbersForTableId(tableId: Int, playedNumbers: Set<Int>) {
        tableBetsMap.put(tableId, playedNumbers)
    }

    override fun fetchTableResultsForTableId(tableId: Int) {
        TODO("Not yet implemented")
    }

    override fun fetchResultsFromNetwork(): String {
        // I want this to be a blocking call and not a suspend method to be blocked on the calling side so...
        return runBlocking {
            coroutineScope.async {
                // While this is a great idea, the dumb API just won't work like this...
//                val yesterday = createYesterdaysDate()
//                val tomorrow = createTomorrowsDate()
//                val resultsJsonArray = apiClient.fetchResults(yesterday, tomorrow)
                // So we will have to do the next best thing - fetching results from today to today
                val today = createTodaysDate()
                val resultsJsonArray = apiClient.fetchResults(today, today)
//                val values = remapJsonArrayToDomainObjects(next20gamesJsonArray)

                // Set our list of games to the fetched results
//                listOfGames.clear()
//                listOfGames.addAll(values)

                //return values
//                values
                resultsJsonArray
            }.await()
        }
    }

    fun createYesterdaysDate(): BaseDate {
        val yesterday = getYesterday()

        return createDate(yesterday.year, yesterday.monthNumber, yesterday.dayOfMonth)
    }

    fun getYesterday(): LocalDateTime {
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val yesterdayDate = currentDateTime.date.minus(1, DateTimeUnit.DAY)
        return LocalDateTime(yesterdayDate, currentDateTime.time)
    }

    fun createTomorrowsDate(): BaseDate {
        val tomorrow = getTomorrow()

        return createDate(tomorrow.year, tomorrow.monthNumber, tomorrow.dayOfMonth)
    }

    fun getTomorrow(): LocalDateTime {
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val tomorrowDate = currentDateTime.date.plus(1, DateTimeUnit.DAY)
        return LocalDateTime(tomorrowDate, currentDateTime.time)
    }

    fun createTodaysDate(): BaseDate {
        val today = getToday()

        return createDate(today.year, today.monthNumber, today.dayOfMonth)
    }

    fun getToday(): LocalDateTime {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }
}
