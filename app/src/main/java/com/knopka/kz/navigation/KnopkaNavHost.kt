package com.knopka.kz.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.knopka.kz.ui.components.WebViewScreen1
import com.knopka.kz.ui.screens.WebViewScreen


@Composable
fun KnopkaNavHost(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            WebViewScreen(url = Screen.Home.url)
        }
        
//        composable(Screen.AddItem.route) {
//            WebViewScreen(url = Screen.AddItem.url)
//        }
//        composable(Screen.Messages.route) {
//            WebViewScreen(url = Screen.Messages.url)
//        }
//        composable(Screen.Profile.route) {
//            WebViewScreen(url = Screen.Profile.url)
//        }
    }
} 