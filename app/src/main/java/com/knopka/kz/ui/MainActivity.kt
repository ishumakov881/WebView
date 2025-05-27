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

import com.knopka.kz.navigation.Screen
import com.knopka.kz.repository.LocalRepository


import com.knopka.kz.ui.screens.WebViewScreen
import com.knopka.kz.ui.theme.KnopkaTheme

import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KnopkaTheme {
                var url: String by remember { mutableStateOf<String>(""/*Screen.Home.url*/) }

<<<<<<< HEAD
                LaunchedEffect(Unit) {
                    val repository = LocalRepository()


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
=======
//                val openFullDialogCustom = remember { mutableStateOf(false) }
//
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//
//                    Column(
//                        modifier = Modifier
//                            .padding(20.dp)
//                            .verticalScroll(rememberScrollState())
//                    ) {
//
//                        //...................................................................
//                        // * full screen custom dialog
//                        Button(
//                            onClick = {
//                                openFullDialogCustom.value = true
//                            },
//                            modifier = Modifier.align(Alignment.CenterHorizontally)
//                        ) {
//                            Text(text = "No internet",style = MaterialTheme.typography.labelLarge)
//                        }
//                    }
//                }
                //...............................................................................
                //Full screen Custom Dialog Sample
                //NoInternetScreen(openFullDialogCustom)
>>>>>>> 0623d9d (Swipe Fixed..)
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

