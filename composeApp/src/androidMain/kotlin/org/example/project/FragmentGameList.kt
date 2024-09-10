package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Preview
@Composable
fun FragmentGameList(navController: NavController) {
    //TODO fetch from api
    val items = listOf("Item 1", "Item 2", "Item 3") // This would be fetched from the API

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

