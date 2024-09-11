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
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import koinModules.commonModule
import koinModules.`interface`.AvailableGamesRepository
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {

    lateinit var tablesRepository: AvailableGamesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidContext(applicationContext)
            modules(commonModule)
        }

        // Inject the tables repository after starting koin
        tablesRepository = get()
        tablesRepository.getAvailableGamesFromNetwork()

        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App Title") },
                actions = {
                    IconButton(onClick = { navController.navigate("fragment1") }) {
                        Icon(Icons.Default.List, contentDescription = "Game List")
                    }
                    IconButton(onClick = { navController.navigate("fragment2") }) {
                        Icon(Icons.Default.Edit, contentDescription = "Betting Table")
                    }
                    IconButton(onClick = { navController.navigate("fragment3") }) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Fragment 3")
                    }
                }
            )
        }
    ) {
        NavHost(navController = navController, startDestination = "fragment1") {
            composable("fragment1") { FragmentGameList(navController) }
            composable("fragment2") { FragmentBettingTable(navController) }
            composable("fragment3") { FragmentDrawResults(navController) }
        }
    }
}
