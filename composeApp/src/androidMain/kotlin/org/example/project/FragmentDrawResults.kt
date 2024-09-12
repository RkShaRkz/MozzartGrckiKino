package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import koinModules.`interface`.TableBetsRepository
import networking.ResultsItem
import org.koin.androidx.compose.get
import util.epochMillisToLocalTime
import util.fillInSetSizeToMatchRowWidth
import util.formatForGameList_DD_MM_HH_MM

@Composable
fun FragmentDrawResults(navController: NavController) {
    val NUMBER_OF_RESULT_COLUMNS = 5 //aka 'the number of items per row'

    val tableBetsRepository: TableBetsRepository = get()

    // Refresh the results
    tableBetsRepository.fetchResultsFromNetwork()


    var results by remember {
        mutableStateOf(tableBetsRepository.getAllResults())
    }

    /**
     * The repository will hold a [drawId, List<Int>] mapping, mapping each drawId to the user's
     * selected numbers; after the results are in which will (or not) be contained in the same repo
     * we will be able to differentiate between these three states for each of the numbers:
     * - player played and was drawn
     * - player played and was not drawn
     * - was drawn and player didn't play it
     *
     * Using these two repositories, we will be able to populate a screen containing a list of
     * draw IDs, their draw time, and the actual 'table' of results.
     */


    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
    ) {
        // This is the list of all results
        items(results) { resultItem ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // This box is a holder for an individual result that needs to have a title and the grid of numbers
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween // Space out elements to the edges
                ) {
                    // This row is the title of the result
                    Text(
                        "Vreme izvlacenja: ${
                            resultItem.drawTime.epochMillisToLocalTime().formatForGameList_DD_MM_HH_MM()
                        } | Kolo: ${resultItem.drawId}"
                    )
                }

                // Extract the winning number list from the results, and played number lists from the repo
                // Then, combine the winning numbers with the actual played numbers into a superset
                // that will be used to display results
                val winningNumbersList = resultItem.winningNumbers.numbersList
                val playedNumbersList = tableBetsRepository.getPlayedNumbersForTableId(resultItem.drawId).toList()
                val allRelevantNumbers = getUnifiedSetOfNumbers(resultItem, tableBetsRepository)
                // We will expand this list so that we end up having a cleanly-divisible number of items
                // with the number of columns, this will avout the UI/UX problem when the last row has
                // eg 1 or 2 items where they grow to be as big a 3-4 rows above it
                val showingList = fillInSetSizeToMatchRowWidth(allRelevantNumbers, NUMBER_OF_RESULT_COLUMNS)

                ResultTable(showingList, NUMBER_OF_RESULT_COLUMNS, winningNumbersList, playedNumbersList)
            }
        }
    }
}

@Composable
fun ResultNumberItem(
    number: Int,
    isInResults: Boolean,
    hasBeenPlayed: Boolean
) {
    // Determine number state
    // If it was played and is in results -     NumberState.HIT
    // If it was played and is not in results - NumberState.MISS
    // If it was not played and is in results - NumberState.NOT_PLAYED
    val numberState = if (isInResults && hasBeenPlayed) {
        NumberState.HIT
    } else if (isInResults && !hasBeenPlayed) {
        NumberState.NOT_PLAYED
    } else if (!isInResults && hasBeenPlayed) {
        NumberState.MISS
    } else {
        NumberState.INSIGNIFICANT
    }

    val backgroundColor = when(numberState) {
        NumberState.HIT -> Color.Green
        NumberState.MISS -> Color.Red
        NumberState.NOT_PLAYED -> Color.LightGray
        NumberState.INSIGNIFICANT -> Color.Transparent
    }

    // Since the only way to have insignificant items is for them to be in neither the played or
    // the results lists, we don't even need to see them. So make their alpha 0 which effectivelly
    // makes them still take up visual space, but being entirely invisible
    val visibilityAlpha = if(numberState == NumberState.INSIGNIFICANT) 0f else 1f

    Surface(
        modifier = Modifier
            .padding(8.dp)
            .alpha(visibilityAlpha)
            .aspectRatio(1f), // Ensure the item is square
        shape = CircleShape,
        color = backgroundColor, // Apply the background color via Surface
        elevation = 4.dp
    ) {
        Box(
            contentAlignment = Alignment.Center, // Center the number
            modifier = Modifier
                .fillMaxSize() // Ensure the Box fills the Surface
                .alpha(visibilityAlpha) // Apply my visibility hack
        ) {
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.h6,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun ResultTable(allRelevantNumbers: List<Int>, numColumns: Int, winningNumbersList: List<Int>, playedNumbersList: List<Int>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            //32.dp cuts into the title box but looks fine everywhere else, so use more on top
            .padding(top = 60.dp, start = 32.dp, end = 32.dp, bottom = 16.dp)
    ) {
        // Create rows out of all relevant numbers
        allRelevantNumbers.chunked(numColumns).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Each item (number) in the row create an individual ResultNumberItem
                rowItems.forEach { number ->
                    Box(
                        modifier = Modifier
                            .weight(1f) // Ensure equal width for each item
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ResultNumberItem(
                            number = number,
                            isInResults = winningNumbersList.contains(number),
                            hasBeenPlayed = playedNumbersList.contains(number)
                        )
                    }
                }
            }
        }
    }
}

fun calculateNumbersCount(resultItem: ResultsItem, tableBetsRepository: TableBetsRepository): Int {
    val winningNumbersSize = resultItem.winningNumbers.numbersList.size
    val playedNumbersSize = tableBetsRepository.getPlayedNumbersForTableId(resultItem.drawId).size

    return winningNumbersSize + playedNumbersSize
}

fun getUnifiedSetOfNumbers(resultItem: ResultsItem, tableBetsRepository: TableBetsRepository): Set<Int> {
    val winningNumbers = resultItem.winningNumbers.numbersList.toSet()
    val playedNumbers = tableBetsRepository.getPlayedNumbersForTableId(resultItem.drawId)

    return winningNumbers + playedNumbers
}

enum class NumberState {
    /**
     * Denotes a state where this number **was played** and **was in results**
     */
    HIT,
    /**
     * Denotes a state where this number **was played** and **was not in results**
     */
    MISS,
    /**
     * Denotes a state where this number **was not played** and **was in results**
     */
    NOT_PLAYED,
    /**
     * Denotes a state where this number **was not played** and **was not in results**
     */
    INSIGNIFICANT
}
