package networking

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class FetchResultsResult(
    @SerialName("content")
    val results: List<ResultsItem>,
    val totalPages: Int,
    val totalElements: Int,
    val last: Boolean,
    val numberOfElements: Int,
    val sort: List<SortItem>,
    val first: Boolean,
    val size: Int,
    val number: Int
)

@Serializable
data class SortItem(
    val direction: String,
    val property: String,
    val ignoreCase: Boolean,
    val nullHandling: String,
    val descending: Boolean,
    val ascending: Boolean
)

@Serializable
data class ResultsItem(
    val gameId: Int,
    val drawId: Int,
    val drawTime: Long,
    @SerialName("status")
    private val stringStatus: String,
    val drawBreak: Int,
    val visualDraw: Int,
    @SerialName("pricePoints")
    val pricePoints: PricePoints,
    @SerialName("winningNumbers")
    val winningNumbers: WinningNumbers,
    @SerialName("prizeCategories")
    val prizeCategoriesList: List<PrizeCategory>? = null,
    @SerialName("wagerStatistics")
    val wagerStatisticsJson: WagerStatistics? = null
) {
    @Transient
    val status: DrawStatus = when(stringStatus) {
        "active" -> DrawStatus.ACTIVE
        "future" -> DrawStatus.FUTURE
        "results" -> DrawStatus.RESULTS
        else -> throw IllegalArgumentException("Status ${stringStatus} not covered by DrawStatus! Please add it")
    }

    companion object {
        // Needed to generate the serializer
    }
}

@Serializable
data class WinningNumbers(
    @SerialName("list")
    val numbersList: List<Int>,
    @SerialName("bonus")
    val bonusNumbersList: List<Int>,
    val sidebets: Sidebets,
    @SerialName("prizeCategories")
    val prizeCategoriesList: List<PrizeCategory>? = null,
    @SerialName("wagerStatistics")
    val wagerStatistics: WagerStatistics? = null
)

@Serializable
data class Sidebets(
    val evenNumbersCount: Int,
    val oddNumbersCount: Int,
    val winningColumn: Int,
    val winningParity: String,
    val oddNumbers: List<Int>,
    val evenNumbers: List<Int>,
    val columnNumbers: Map<String, List<Int>>
)
