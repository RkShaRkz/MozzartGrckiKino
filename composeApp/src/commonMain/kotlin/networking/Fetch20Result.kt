package networking

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

data class Fetch20Result(
    val gameId: Int,
    val drawId: Int,
    val drawTime: Long,
    private val stringStatus: String,
    val drawBreak: Int,
    val visualDraw: Int,
    val pricePointsJson: String,
    val prizeCategoriesJsonArray: String,
    val wagerStatisticsJson: String
) {
    val status: DrawStatus = when(stringStatus) {
        "active" -> DrawStatus.ACTIVE
        "future" -> DrawStatus.FUTURE
        else -> throw IllegalArgumentException("Status ${stringStatus} not covered by DrawStatus! Please add it")
    }
}

enum class DrawStatus { ACTIVE, FUTURE }
