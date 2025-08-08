package com.knopka.kz.ui

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.knopka.kz.R


import com.knopka.kz.navigation.Screen
import com.knopka.kz.navigation.Screen.Companion.bottomNavItems
import com.knopka.kz.ui.components.KnopkaBottomNavigation
import com.knopka.kz.ui.screens.WebViewScreen
import com.knopka.kz.ui.screens.OnboardingScreen
import androidx.core.content.edit
import com.knopka.kz.ui.screens.AboutScreen
import com.knopka.kz.ui.screens.NotificationsScreen
import com.knopka.kz.ui.screens.ProfileScreen

data class WebViewControls(
    val canGoBack: Boolean,
    val canGoForward: Boolean,
    val reload: () -> Unit,
    val goBack: () -> Unit,
    val goForward: () -> Unit
)

const val TOP_BAR_ENABLED = false

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KnopkaApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val showBackButton = navController.previousBackStackEntry != null
    val webViewControlsMap = remember { mutableStateMapOf<String, WebViewControls?>() }
    val screenTitle = bottomNavItems.find { it.screen.route == currentRoute }?.label ?: ""

    val context = LocalContext.current
    val def = booleanResource(R.bool.show_onboarding)
    var showOnboarding by remember { mutableStateOf(!def) }


    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        showOnboarding = !prefs.getBoolean("onboarding_shown", false)
    }

    if (showOnboarding && def) {
        OnboardingScreen(onFinish = {
            val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            prefs.edit { putBoolean("onboarding_shown", true) }
            showOnboarding = false
        })
    } else {
        Scaffold(
            topBar = {
                if (TOP_BAR_ENABLED)
                    TopAppBar(
                        title = {
                            Column {
                                Text(
                                    text = stringResource(R.string.app_name),
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = screenTitle,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        },
                        actions = {
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
                                IconButton(
                                    onClick = {
                                        if (currentRoute != Screen.HomeScreen.route) {
                                            navController.navigate(Screen.HomeScreen.route) {
                                                popUpTo(navController.graph.startDestinationId)
                                            }
                                        }
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Home,
                                        contentDescription = "Home"
                                    )
                                }
                            }
                        }
                    ) else null
            },
            bottomBar = {
                if (bottomNavItems.size > 1)
                    KnopkaBottomNavigation(
                        currentRoute = currentRoute,
                        onNavigate = { screen ->
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    ) else null
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                KnopkaNavHost(controller = navController, webViewControlsMap = webViewControlsMap)
            }
        }
    }
}


@Composable
fun KnopkaNavHost(
    controller: NavHostController,
    webViewControlsMap: MutableMap<String, WebViewControls?>,
    startDestination: String = Screen.HomeScreen.route
) {

    val context = LocalContext.current

    NavHost(navController = controller, startDestination = startDestination) {

        bottomNavItems.forEach { screen ->
            when (screen.screen) {
                Screen.AddItem -> {}
                Screen.HomeScreen -> {
                    composable(Screen.HomeScreen.route) {


//                        BackHandler(onBack = {
//                            Toast.makeText(context, "@@@@@", Toast.LENGTH_SHORT).show()
//                        })


                        WebViewScreen(
                            url = Screen.HomeScreen.url ?: "",
                            onControlsChanged = {
                                webViewControlsMap[Screen.HomeScreen.route] = it
                            },
                            onBackPressed = {
                                controller.popBackStack() // Выходим из экрана
                            }
                        )
                    }
                }

                Screen.NotificationsScreen -> {
                    composable(Screen.NotificationsScreen.route) {
                        NotificationsScreen()
                    }
                }

                Screen.ProfileScreen -> {
                    composable(Screen.ProfileScreen.route) {
                        ProfileScreen(controller)
                    }
                }
            }
            // Экран 'О приложении'
            composable("about") {
                AboutScreen(onBack = { controller.popBackStack() })
            }
//            composable(screen.first.route) {
//                WebViewScreen(
//                    url = screen.first.url,
//                    onControlsChanged = { webViewControlsMap[screen.first.route] = it }
//                )
//            }
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

