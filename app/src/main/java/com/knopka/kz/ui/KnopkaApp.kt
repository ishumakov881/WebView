package com.knopka.kz.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.knopka.kz.R

import com.knopka.kz.navigation.Screen
import com.knopka.kz.ui.components.KnopkaBottomNavigation
import com.knopka.kz.ui.screens.WebViewScreen

data class WebViewControls(
    val canGoBack: Boolean,
    val canGoForward: Boolean,
    val reload: () -> Unit,
    val goBack: () -> Unit,
    val goForward: () -> Unit
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KnopkaApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val showBackButton = navController.previousBackStackEntry != null

    //val refreshTriggers = remember { mutableStateMapOf<String, Boolean>() }
    val webViewControlsMap = remember { mutableStateMapOf<String, WebViewControls?>() }
    val screenTitle = Screen.bottomNavItems.find { it.first.route == currentRoute }?.second ?: ""


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = stringResource(R.string.app_name), style = MaterialTheme.typography.titleLarge)
                        Text(
                            text = screenTitle,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                actions = {
                    // Общие действия для всех экранов
//                    IconButton(onClick = {
//                        refreshTriggers[currentRoute] = !(refreshTriggers[currentRoute] ?: false)
//                    }) {
//                        Icon(Icons.Default.Refresh, "Refresh")
//                    }
                    val controls = webViewControlsMap[currentRoute]


                    if (controls?.canGoBack == true) {
                        IconButton(onClick = { controls.goBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                        }
                    }

                    if (controls?.canGoForward == true) {
                        IconButton(onClick = { controls.goForward() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, "Forward")
                        }
                    }

                    IconButton(onClick = { controls?.reload?.invoke() }) {
                        Icon(Icons.Default.Refresh, "Refresh")
                    }

                },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Back"
                            )
                        }
                    } else {
                        // Кнопка "Домой" когда нет backstack
                        IconButton(
                            onClick = {
                                if (currentRoute != Screen.Home.route) {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(navController.graph.startDestinationId)
                                    }
                                }

                            },
                            //enabled =  Screen.Home.route!=currentRoute
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = "Home"
                            )
                        }
                    }
                }
//                backgroundColor = MaterialTheme.colors.primary,
//                elevation = 4.dp
            )
        },
        bottomBar = {
            KnopkaBottomNavigation(
                currentRoute = currentRoute,
                onNavigate = { screen ->
                    navController.navigate(screen.route) {
                        // Очищаем бэкстек до выбранного экрана
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Избегаем дублирования экранов в стеке
                        launchSingleTop = true
                        // Восстанавливаем состояние при возврате
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            KnopkaNavHost(navController = navController, webViewControlsMap = webViewControlsMap)
        }
    }
}


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
        composable(Screen.AddItem.route) {
            WebViewScreen(
                url = Screen.AddItem.url,
                onControlsChanged = { webViewControlsMap[Screen.AddItem.route] = it }
            )
        }
        composable(Screen.Messages.route) {
            WebViewScreen(
                url = Screen.Messages.url,
                onControlsChanged = { webViewControlsMap[Screen.Messages.route] = it }
            )
        }
        composable(Screen.Profile.route) {
            WebViewScreen(
                url = Screen.Profile.url,
                onControlsChanged = { webViewControlsMap[Screen.Profile.route] = it }
            )
        }
    }
}

