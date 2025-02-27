package com.knopka.kz.ui

import android.os.Build
import android.os.Build.*
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.knopka.kz.navigation.Screen
import com.knopka.kz.repository.GoogleSpreadheetsRepository

import com.knopka.kz.ui.components.WebViewScreen1
import com.knopka.kz.ui.screens.MySplashScreen
import com.knopka.kz.ui.screens.WebViewScreen
import com.knopka.kz.ui.theme.KnopkaTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KnopkaTheme {
                var url: String by remember { mutableStateOf<String>(""/*Screen.Home.url*/) }

                LaunchedEffect(Unit) {
                    val repository = GoogleSpreadheetsRepository()
                    launch {
                        url = repository.fetchUrlFromGoogleDrive() ?: Screen.Home.url
                    }
                }

                if (!url.isNullOrEmpty()) {
                    WebViewScreen(url = url)
                }
//                else {
//                    MySplashScreen()
//                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        //hideSystemUI()
    }

    private fun hideSystemUI() {
        if (VERSION.SDK_INT >= VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE)
                it.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
        }
    }
}

