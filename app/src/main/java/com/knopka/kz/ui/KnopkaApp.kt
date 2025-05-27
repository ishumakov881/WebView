package com.knopka.kz.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.knopka.kz.navigation.KnopkaNavHost
import com.knopka.kz.navigation.Screen
import com.knopka.kz.ui.components.KnopkaBottomNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KnopkaApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val showBackButton = navController.previousBackStackEntry != null


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WebView App") },
                actions = {
                    // Общие действия для всех экранов
                    IconButton(onClick = { /* Обновить страницу */ }) {
                        Icon(Icons.Default.Refresh, "Refresh")
                    }
                },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
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
            KnopkaNavHost(navController = navController)
        }
    }
} 