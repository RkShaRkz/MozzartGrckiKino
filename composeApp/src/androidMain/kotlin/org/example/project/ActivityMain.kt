package org.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import koinModules.`interface`.AvailableGamesRepository
import koinModules.`interface`.TableBetsRepository
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    val tablesRepository: AvailableGamesRepository by inject<AvailableGamesRepository>()
    val tableBetsRepository: TableBetsRepository by inject<TableBetsRepository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tablesRepository.getAvailableGamesFromNetwork()
        tableBetsRepository.fetchResultsFromNetwork()

        setContent {
            MainScreen()
        }
    }

    companion object {
        const val BETTING_TABLE_ICON_ENABLED = false
    }
}



@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Grcki Kino") },
                actions = {
                    IconButton(onClick = { navController.navigate("fragment1") }) {
                        Icon(Icons.Default.List, contentDescription = "Game List")
                    }
                    if (MainActivity.BETTING_TABLE_ICON_ENABLED) {
                        IconButton(onClick = { navController.navigate("fragment2") }) { //NO BOARD ID
                            Icon(Icons.Default.Edit, contentDescription = "Betting Table")
                        }
                    }
                    IconButton(onClick = { navController.navigate("fragment3") }) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Results List")
                    }
                    IconButton(onClick = { navController.navigate("fragment4") }) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Watch Draw")
                    }
                }
            )
        }
    ) {
        NavigationGraph(navController = navController)
    }
}
