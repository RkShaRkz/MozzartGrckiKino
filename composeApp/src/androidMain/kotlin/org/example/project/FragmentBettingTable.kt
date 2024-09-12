package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import koinModules.`interface`.AvailableGamesRepository
import koinModules.`interface`.TableBetsRepository
import org.koin.androidx.compose.get
import org.koin.compose.koinInject
import util.epochMillisToLocalTime
import util.formatForGameList_DD_MM_HH_MM
import util.formatForGameList_HH_MM

@Composable
fun FragmentBettingTable(navController: NavController, drawId: Int) {
    val numRows = 10
    val numColumns = 8
    val numberOfItems = numRows * numColumns

    val tableBetsRepository: TableBetsRepository = get()
    val availableGamesRepository: AvailableGamesRepository = get()

    val TABLE_ID = drawId
    val currentGame = availableGamesRepository.findGameByDrawId(drawId)

    val MAXIMUM_SELECTED_ITEMS = 15

    var selectedItems by remember {
        mutableStateOf(tableBetsRepository.getPlayedNumbersForTableId(TABLE_ID))
    }

    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween // Space out elements to the edges
        ) {
            Text("Vreme izvlacenja: ${currentGame?.drawTime?.epochMillisToLocalTime()?.formatForGameList_HH_MM()} | Kolo: ${drawId}")
        }

        Column(
            modifier = Modifier
                .padding(start = 0.dp, top = 52.dp, end = 0.dp, bottom = 8.dp)
                .align(Alignment.TopCenter)
        ) {
            // First Row: NUMBER
            Row(modifier = Modifier
                .fillMaxWidth()
            ) {
                Text(
                    text = "B. K.",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                repeat(8) { index ->
                    Text(
                        text = (index + 1).toString(),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp)) // Space between rows

            // Second Row: COEF.
            Row(modifier = Modifier
                .fillMaxWidth()
            ) {
                Text(
                    text = "KVOTA",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                val coefficients = listOf(1.25, 1.60, 2.00, 2.70, 3.70, 5.00, 7.00, 9.50)
                coefficients.forEach { coef ->
                    Text(
                        text = coef.toString(),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(numColumns),
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 120.dp, end = 16.dp, bottom = 16.dp)       //96 is just 'barely there'
        ) {
            items(numberOfItems) { number ->
                NumberItem(
                    number = number + 1, // Make it 1 to 80 instead of 0 to 79
                    isSelected = selectedItems.contains(number + 1),
                    onClick = {
                        selectedItems = if (selectedItems.contains(number + 1)) {
                            selectedItems - (number + 1) // Deselect the item
                        } else {
                            // Select only if we're below maximum selected items
                            if (selectedItems.size < MAXIMUM_SELECTED_ITEMS) {
                                selectedItems + (number + 1) // Select the item
                            } else {
                                // Do nothing
                                selectedItems
                            }
                        }

                        tableBetsRepository.putPlayedNumbersForTableId(TABLE_ID, selectedItems)
                    }
                )
            }
        }

        // Show a message if max selection is reached
        if (selectedItems.size == MAXIMUM_SELECTED_ITEMS) {
            Text(
                text = "You have reached the maximum selection of 15 items",
                color = Color.Red,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomCenter)
            )
        } else {
            Text(
                text = "Selected items: ${selectedItems.size}",
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun NumberItem(
    number: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color.Green else Color.LightGray

    Surface(
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(1f) // Ensure the item is square
            .clickable(onClick = onClick),
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
