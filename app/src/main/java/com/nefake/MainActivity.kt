package com.nefake

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.knopka.kz.ui.components.WebViewScreen1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        // Request notification permissions for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(POST_NOTIFICATIONS),
                    1
                )
            }
        }

        setContent{
            WebViewScreen1()
        }
//        setContent {
//            var currentScreen by remember { mutableStateOf<Screen>(Screen.WebView) }
//            var currentUrl by remember { mutableStateOf(Constants.BASE_URL) }
//            val isDarkTheme = isSystemInDarkTheme()
//
//            // Обработка кнопки назад
//            BackHandler(enabled = currentScreen is Screen.Rss) {
//                currentScreen = Screen.WebView
//            }
//
//            when (currentScreen) {
//                Screen.WebView -> WebViewScreen(
//                    url = currentUrl,
//                    onRssClick = { currentScreen = Screen.Rss },
//                    isDarkTheme = isDarkTheme
//                )
//                Screen.Rss -> RssScreen(
//                    url = "${Constants.BASE_URL}rss",
//                    onItemClick = { rssItem ->
//                        currentUrl = rssItem.link
//                        currentScreen = Screen.WebView
//                    },
//                    isDarkTheme = isDarkTheme
//                )
//            }
//        }
    }
}

sealed class Screen {
    object WebView : Screen()
    object Rss : Screen()
} 