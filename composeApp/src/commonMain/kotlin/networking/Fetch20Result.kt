package networking

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * {
 * gameId: 1100, - ID Igre
 * drawId: 838524, - ID izvlačenja
 * za Grčki kino
 * drawTime: 1602580200000, - vreme izvlačenja
 * status: "active",
 * drawBreak: 0,
 * visualDraw: 838524,
 * pricePoints: {},
 * prizeCategories: [],
 * wagerStatistics: {},
 * }
 */

@Serializable
data class Fetch20Result(
    val gameId: Int,
    val drawId: Int,
    val drawTime: Long,
    @SerialName("status")
    private val stringStatus: String,
    val drawBreak: Int,
    val visualDraw: Int,
    @SerialName("pricePoints")
    val pricePoints: PricePoints,
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

/**
 * "pricePoints": {
 *             "addOn": [
 *                 {
 *                     "amount": 0.5,
 *                     "gameType": "KenoCloseToWin"
 *                 },
 *                 {
 *                     "amount": 0.5,
 *                     "gameType": "KinoBonus"
 *                 },
 *                 {
 *                     "amount": 0.5,
 *                     "gameType": "SideBets"
 *                 }
 *             ],
 *             "amount": 0.5
 *         }
 */
@Serializable
data class PricePoints(
    @SerialName("addOn")
    val addons: List<PricePointAddon>,
    @SerialName("amount")
    val amount: Float
)

@Serializable
data class PricePointAddon(
    @SerialName("amount")
    val amount: Float,
    @SerialName("gameType")
    val gameType: String
)

/**
 *             {
 *                 "id": 1,
 *                 "divident": 0,
 *                 "winners": 0,
 *                 "distributed": 0,
 *                 "jackpot": 0,
 *                 "fixed": 1,
 *                 "categoryType": 2,
 *                 "gameType": "KinoBonus"
 *             }
 */
@Serializable
data class PrizeCategory(
    val id: Int,
    val divident: Float,
    val winners: Int,
    val distributed: Float,
    val jackpot: Float,
    val fixed: Float,
    val categoryType: Int,
    val gameType: String
)

/**
 * "wagerStatistics": {
 *             "columns": 0,
 *             "wagers": 0,
 *             "addOn": []
 *         }
 */
@Serializable
data class WagerStatistics(
    val columns: Int,
    val wagers: Int,
    val addOn: List<DummyValue?>? = null
)

@Serializable
data class DummyValue(
    val bla: Int    //hopefully these wont happen
)

enum class DrawStatus { ACTIVE, FUTURE, RESULTS }
