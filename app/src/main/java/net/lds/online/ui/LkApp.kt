package net.lds.online.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lds.wvrss.R
import net.lds.online.navigation.KnopkaNavHost


data class WebViewControls(
    val canGoBack: Boolean,
    val canGoForward: Boolean,
    val reload: () -> Unit,
    val goBack: () -> Unit,
    val goForward: () -> Unit
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LkApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    //val refreshTriggers = remember { mutableStateMapOf<String, Boolean>() }
    val webViewControlsMap = remember { mutableStateMapOf<String, WebViewControls?>() }

    Scaffold(
//        topBar = {
//            WebVieToolbar(
//                title = Screen.bottomNavItems.find { it.first.route == currentRoute }?.second ?: "",
//                controls = webViewControlsMap[currentRoute],
//                showBackButton = navController.previousBackStackEntry != null,
//                onBack = {
//                    navController.navigateUp()
//                }, onHome = {
//                    if (currentRoute != Screen.Home.route) {
//                        navController.navigate(Screen.Home.route) {
//                            popUpTo(navController.graph.startDestinationId)
//                        }
//                    }
//                })
//        },
//        bottomBar = {
//            KnopkaBottomNavigation(
//                currentRoute = currentRoute,
//                onNavigate = { screen ->
//                    navController.navigate(screen.route) {
//                        // Очищаем бэкстек до выбранного экрана
//                        popUpTo(navController.graph.startDestinationId) {
//                            saveState = true
//                        }
//                        // Избегаем дублирования экранов в стеке
//                        launchSingleTop = true
//                        // Восстанавливаем состояние при возврате
//                        restoreState = true
//                    }
//                }
//            )
//        }
    ) {
        paddingValues ->
        println(paddingValues)
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            KnopkaNavHost(navController = navController, webViewControlsMap = webViewControlsMap)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebVieToolbar(
    title: String, controls: WebViewControls?, showBackButton: Boolean,
    onBack: () -> Unit, onHome: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = title,
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
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Back"
                    )
                }
            } else {
                // Кнопка "Домой" когда нет backstack
                IconButton(
                    onClick = onHome,
                    //enabled =  Screen.Home.route!=currentRoute
                ) {
                    Icon(imageVector = Icons.Filled.Home, contentDescription = "Home")
                }
            }
        }
//                backgroundColor = MaterialTheme.colors.primary,
//                elevation = 4.dp
    )
}


