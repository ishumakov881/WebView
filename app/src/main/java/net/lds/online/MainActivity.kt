//package net.lds.online
//
//import android.os.Bundle
//import android.view.WindowManager
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.compose.BackHandler
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.runtime.*
//import androidx.core.view.WindowCompat
//import net.lds.online.core.Constants
//
//import net.online.wvrss.ui.RssScreen
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
//
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
//                Screen.WebView -> WebViewScreen0(
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
//    }
//}
//
//sealed class Screen {
//    object WebView : Screen()
//    object Rss : Screen()
//}