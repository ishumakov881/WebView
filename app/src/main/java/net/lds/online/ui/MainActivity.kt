package net.lds.online.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import net.lds.online.ui.theme.AppTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Правильная обработка инсетов для edge-to-edge дизайна
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        
        setContent {
            AppTheme {

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
} 