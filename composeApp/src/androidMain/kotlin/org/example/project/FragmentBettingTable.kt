package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun FragmentBettingTable(navController: NavController) {
    //TODO move to repository
    var selectedItems by remember { mutableStateOf(setOf<Int>()) }

    Column(modifier = Modifier.padding(16.dp)) {
        for (i in 1..80) {
            Button(
                onClick = {
                    if (i in selectedItems) {
                        selectedItems = selectedItems - i
                    } else {
                        selectedItems = selectedItems + i
                    }
                },
                modifier = Modifier
                    .padding(4.dp)
                    .size(40.dp)
                    .background(if (i in selectedItems) Color.Green else Color.Gray, CircleShape)
            ) {
                Text(text = "$i")
            }
        }
    }
}

