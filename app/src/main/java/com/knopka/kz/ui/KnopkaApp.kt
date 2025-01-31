package com.knopka.kz.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.knopka.kz.navigation.KnopkaNavHost
import com.knopka.kz.ui.components.KnopkaBottomNavigation

@Composable
fun KnopkaApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    Scaffold(
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