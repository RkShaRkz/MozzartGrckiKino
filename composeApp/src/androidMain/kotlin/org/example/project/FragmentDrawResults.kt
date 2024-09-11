package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun FragmentDrawResults(navController: NavController) {
    //TODO get these from Repository for each of these draws
    val playerSelectedItems = listOf(1, 5, 20) // Example selected items
    //TODO make an API call for each drawId contained in the repository's map (keySet)
    val apiResults = listOf(1, 5, 8, 12) // Example fetched results

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

    Column(modifier = Modifier.padding(16.dp)) {
        for (i in 1..80) {
            val color = when {
                i in playerSelectedItems && i in apiResults -> Color.Green
                i in playerSelectedItems && i !in apiResults -> Color.Red
                i !in playerSelectedItems && i in apiResults -> Color.Gray
                else -> Color.White
            }

            Text(
                text = "$i",
                modifier = Modifier
                    .padding(4.dp)
                    .background(color)
                    .fillMaxWidth()
            )
        }
    }
}

