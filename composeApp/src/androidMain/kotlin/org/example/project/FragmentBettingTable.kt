package org.example.project

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import koinModules.`interface`.TableBetsRepository
import org.koin.androidx.compose.get
import org.koin.compose.koinInject

@Composable
fun FragmentBettingTable(navController: NavController, drawId: Int) {
    val numRows = 10
    val numColumns = 8
    val numberOfItems = numRows * numColumns

    val tableBetsRepository: TableBetsRepository = get()

    val TABLE_ID = drawId

    var selectedItems by remember {
        mutableStateOf(tableBetsRepository.getPlayedNumbersForTableId(TABLE_ID))
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(numColumns), // Creates 10 columns
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(numberOfItems) { number ->
            NumberItem(
                number = number + 1, // Make it 1 to 80 instead of 0 to 79
                isSelected = selectedItems.contains(number + 1),
                onClick = {
                    selectedItems = if (selectedItems.contains(number + 1)) {
                        selectedItems - (number + 1) // Deselect the item
                    } else {
                        selectedItems + (number + 1) // Select the item
                    }

                    tableBetsRepository.putPlayedNumbersForTableId(TABLE_ID, selectedItems)
                }
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
