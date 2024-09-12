package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import koinModules.`interface`.AvailableGamesRepository
import koinModules.`interface`.TableBetsRepository
import networking.ResultsItem
import org.koin.androidx.compose.get
import util.epochMillisToLocalTime
import util.formatForGameList_HH_MM

@Composable
fun FragmentDrawResults(navController: NavController) {
    val NUMBER_OF_RESULT_COLUMNS = 5 //aka 'the number of items per row'

    val tableBetsRepository: TableBetsRepository = get()
    val availableGamesRepository: AvailableGamesRepository = get()

    // Refresh the results
    tableBetsRepository.fetchResultsFromNetwork()


    var results by remember {
        mutableStateOf(tableBetsRepository.getAllResults())
    }

    var tableIdsFromResults by remember {
        mutableStateOf(tableBetsRepository.getAllResults().map { result -> result.drawId })
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
                            resultItem.drawTime.epochMillisToLocalTime().formatForGameList_HH_MM()
                        } | Kolo: ${resultItem.drawId}"
                    )
                }

                // Extract the winning number list from the results, and played number lists from the repo
                // Then, combine the winning numbers with the actual played numbers into a superset
                // that will be used to display results
                val winningNumbersList = resultItem.winningNumbers.numbersList
                val playedNumbersList = tableBetsRepository.getPlayedNumbersForTableId(resultItem.drawId).toList()
                //TODO expand this set so that it fills up the whole row because this can look very ugly
                // when the last row has e.g. 2 items and they take up a whole lot of space and disturb the UI/UX
                val allRelevantNumbers = getUnifiedSetOfNumbers(resultItem, tableBetsRepository)

                ResultTable(allRelevantNumbers.toList(), 5, winningNumbersList, playedNumbersList)
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

    Surface(
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(1f), // Ensure the item is square
        shape = CircleShape,
        color = backgroundColor, // Apply the background color via Surface
        elevation = 4.dp
    ) {
        Box(
            contentAlignment = Alignment.Center, // Center the number
            modifier = Modifier.fillMaxSize() // Ensure the Box fills the Surface
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
//            .padding(32.dp) //16.dp is too small    //32.dp cuts into the title box
            .padding(top = 48.dp, start = 32.dp, end = 32.dp, bottom = 32.dp)   //32.dp cuts into the title box but looks fine everywhere else, so use more on top
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
//                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
//                        Text(text = number.toString())
//                        android.util.Log.wtf("SHARK", "rowItems: ${rowItems}, number: ${number}")
//                        val numberItem = number //if (number < allRelevantNumbers.size) allRelevantNumbers.get(number) else 0
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
