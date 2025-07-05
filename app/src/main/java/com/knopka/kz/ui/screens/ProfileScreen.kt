package com.knopka.kz.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiryadev.bootstrapiconscompose.BootstrapIcons
import com.wiryadev.bootstrapiconscompose.bootstrapicons.Normal
import com.wiryadev.bootstrapiconscompose.bootstrapicons.normal.Instagram
import com.wiryadev.bootstrapiconscompose.bootstrapicons.normal.Telegram
import com.wiryadev.bootstrapiconscompose.bootstrapicons.normal.Whatsapp
import androidx.navigation.NavController
import androidx.core.net.toUri
import android.widget.Toast

fun safeStartActivity(context: Context, intent: Intent) {
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Не удалось открыть", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val packageName = context.packageName

    val items = listOf(
        ContactItem(Icons.Default.Public, "Вконтакте") {
            val intent = Intent(Intent.ACTION_VIEW, "https://vk.com/positivefoto".toUri())
            safeStartActivity(context, intent)
        },
        ContactItem(BootstrapIcons.Normal.Instagram, "Instagram") {
            val intent = Intent(Intent.ACTION_VIEW,
                "https://www.instagram.com/positivefoto/".toUri())
            safeStartActivity(context, intent)
        },
        ContactItem(BootstrapIcons.Normal.Whatsapp, "WhatsApp") {
            val intent = Intent(Intent.ACTION_VIEW, "https://wa.me/+79276410707".toUri())
            safeStartActivity(context, intent)
        },
        ContactItem(BootstrapIcons.Normal.Telegram, "Telegram") {
            val intent = Intent(Intent.ACTION_VIEW, "https://t.me/positivephoto24".toUri())
            safeStartActivity(context, intent)
        },
        ContactItem(Icons.Default.Email, "Написать на email") {
            val intent = Intent(Intent.ACTION_SENDTO, "mailto:ceo@positivefranchise.ru".toUri())
            safeStartActivity(context, intent)
        },
        ContactItem(Icons.Default.Share, "Поделиться") {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Скачать приложение: https://play.google.com/store/apps/details?id=$packageName")
            }
            safeStartActivity(context, Intent.createChooser(intent, "Поделиться"))
        },
        ContactItem(Icons.Default.Star, "Оценить приложение") {
            val intent = Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri())
            safeStartActivity(context, intent)
        },
        ContactItem(Icons.Default.Info, "О приложении") {
            navController.navigate("about")
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Контакты",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Divider()
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(items) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { item.onClick() }
                        .padding(horizontal = 16.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
                Divider()
            }
        }
    }
}

data class ContactItem(
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit
) 