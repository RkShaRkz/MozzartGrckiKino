package org.example.project

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "fragment1") {
        composable("fragment1") {
            FragmentGameList(navController)
        }
        // This is for the action bar button
//        composable("fragment2") { backStackEntry ->
//            FragmentBettingTable(navController, -1)
//        }
        composable("fragment2/{boardId}") { backStackEntry ->
            backStackEntry.arguments?.let {
                // Why is this so stupid...
                val boardId = it.getString("boardId", "-1").toInt()
                FragmentBettingTable(navController, boardId)
            }
        }
        composable("fragment3") {
            FragmentDrawResults(navController)
        }
        composable("fragment4") {
            WebViewScreen("https://mozzartbet.com/sr/lotto-animation/26#")
        }
    }
}
