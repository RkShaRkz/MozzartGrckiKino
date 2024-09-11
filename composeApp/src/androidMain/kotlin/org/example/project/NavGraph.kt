package org.example.project

import androidx.appcompat.app.AppCompatActivity
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
            /*
            FragmentWebView.newInstance(
                "https://mozzartbet.com/sr/lotto-animation/26#",
                navController
            )
                .apply {
                    // Create and set the fragment as required
                    val fragmentManager = (context as AppCompatActivity).supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, this)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
             */
            WebViewScreen("https://mozzartbet.com/sr/lotto-animation/26#")
        }
    }
}
