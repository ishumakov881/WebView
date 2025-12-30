package net.lds.online.ui

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import com.lds.webview.BuildConfig
import com.walhalla.nointernet.NoInternetScreen
import kotlinx.coroutines.FlowPreview

import net.lds.online.ui.theme.AppTheme


class MainActivity : ComponentActivity() {

    @OptIn(FlowPreview::class)
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        if (BuildConfig.DEBUG) {
            window.addFlags( //WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or //WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }
        setContent {
            AppTheme {

//                val openFullDialogCustom = remember { mutableStateOf(false) }
//                val isNetworkAvailable by rememberNetworkStatus()
//
//                LaunchedEffect(Unit) {
//                    snapshotFlow { isNetworkAvailable }
//                        .debounce(500)
//                        .distinctUntilChanged()
//                        .collect { status ->
//                            when (status) {
//                                NetworkStatus.Lost, NetworkStatus.Unavailable, NetworkStatus.NoInternet -> {
//                                    openFullDialogCustom.value = true
//                                }
//                                NetworkStatus.Available -> {
//                                    openFullDialogCustom.value = false
//                                }
//                                else -> { /* Do nothing */ }
//                            }
//                        }
//                }

                Box {
                    LkApp()

                    //println("@@@@ isNetworkAvailable @@@@ $isNetworkAvailable")
                    //Text(color = Color.Red, text = "@@@@ $isNetworkAvailable")

                }

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
//                if (openFullDialogCustom.value) {
//                    NoInternetScreen(onDismiss = {
//                        openFullDialogCustom.value = false
//                    })
//                }
            }

        }
    }
}