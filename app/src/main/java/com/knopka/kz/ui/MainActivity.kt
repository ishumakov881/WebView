package com.knopka.kz.ui

import android.os.Bundle
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.knopka.kz.ui.theme.KnopkaTheme
import android.content.Context

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveFirstLaunchTimeIfNeeded()
        
        // Правильная обработка инсетов для edge-to-edge дизайна
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        setContent {
            KnopkaTheme {
                KnopkaApp()

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
            }
        }
    }

    private fun saveFirstLaunchTimeIfNeeded() {
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        if (!prefs.contains("first_launch_time")) {
            val now = System.currentTimeMillis()
            prefs.edit().putLong("first_launch_time", now).apply()
        }
    }
} 