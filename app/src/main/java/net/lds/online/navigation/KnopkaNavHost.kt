package net.lds.online.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import net.lds.online.ui.WebViewControls
import net.lds.online.ui.screens.WebViewScreen

@Composable
fun KnopkaNavHost(
    navController: NavHostController,
    webViewControlsMap: MutableMap<String, WebViewControls?>,
    startDestination: String = Screen.Home.route
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Home.route) {
            WebViewScreen(
                url = Screen.Home.url,
                onControlsChanged = { webViewControlsMap[Screen.Home.route] = it }
            )
        }
//        composable(Screen.AddItem.route) {
//            WebViewScreen(
//                url = Screen.AddItem.url,
//                onControlsChanged = { webViewControlsMap[Screen.AddItem.route] = it }
//            )
//        }
//        composable(Screen.Messages.route) {
//            WebViewScreen(
//                url = Screen.Messages.url,
//                onControlsChanged = { webViewControlsMap[Screen.Messages.route] = it }
//            )
//        }
//        composable(Screen.Profile.route) {
//            WebViewScreen(
//                url = Screen.Profile.url,
//                onControlsChanged = { webViewControlsMap[Screen.Profile.route] = it }
//            )
//        }
    }
}