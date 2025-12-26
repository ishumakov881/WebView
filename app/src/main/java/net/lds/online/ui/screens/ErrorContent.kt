package net.lds.online.ui.screens

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.imaginativeworld.oopsnointernet.utils.NoInternetUtils

@Composable
fun ErrorContent(isLoading: Boolean, onReload: () -> Unit) {
    val context = LocalContext.current
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.Companion.fillMaxSize().padding(16.dp),
            contentAlignment = Alignment.Companion.Center
        ) {
            Column(
                horizontalAlignment = Alignment.Companion.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Нет подключения к интернету",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Companion.Center
                )

                Text(
                    text = "Проверьте подключение и попробуйте снова",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Companion.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Button(onClick = { NoInternetUtils.turnOnWifi(context) }) {
                    Text("Открыть настройки сети")
                }

                OutlinedButton(
                    enabled = !isLoading,
                    onClick = onReload
                ) {
                    Box{
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
                            Text("Повторить")
                        }
                    }
                }
            }
        }
    }
}