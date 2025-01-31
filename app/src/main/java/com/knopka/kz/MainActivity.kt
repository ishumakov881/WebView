package com.knopka.kz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.knopka.kz.ui.KnopkaApp
import com.knopka.kz.ui.theme.KnopkaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Правильная обработка инсетов для edge-to-edge дизайна
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        setContent {
            KnopkaTheme {
                KnopkaApp()
            }
        }
    }
} 