package org.example.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import koinModules.`interface`.AvailableGamesRepository
import kotlinx.coroutines.delay
import org.koin.androidx.compose.get

@Preview
@Composable
fun FragmentGameList(navController: NavController) {
    val tablesRepository: AvailableGamesRepository = get()
    //TODO fetch from api
//    val items = listOf("Item 1", "Item 2", "Item 3") // This would be fetched from the API
    val items by remember {
        mutableStateOf(tablesRepository.getAvailableGames().map { result -> "${result.gameId} - ${result.status}" })
    }

    LazyColumn {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = item)
                CountdownTimer(remainingSeconds = 60) // Example countdown timer
            }

            Divider()
        }
    }
}

@Composable
fun CountdownTimer(remainingSeconds: Int) {
    var secondsLeft by remember { mutableStateOf(remainingSeconds) }

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000L)
            secondsLeft -= 1
        }
    }

    Text(text = "Time left: $secondsLeft seconds")
}

