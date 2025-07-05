package com.knopka.kz.ui.screens

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.knopka.kz.R
import androidx.core.net.toUri
import com.knopka.kz.BuildConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val pm = context.packageManager
    val packageName = context.packageName
    val versionName = remember {
        try {
            BuildConfig.VERSION_NAME
        } catch (e: Exception) {
            "1.0.0"
        }
    }
    val versionCode = remember {
        try {
            BuildConfig.VERSION_CODE.toLong()
        } catch (e: Exception) {
            1L
        }
    }
    val buildVersion = "4.0.0" // Можно вынести в BuildConfig или gradle

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("О приложении") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_logo_round),
                contentDescription = "App Icon",
                modifier = Modifier.size(96.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Версия $versionName ($versionCode)",

                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Версия сборки $buildVersion",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                textAlign = TextAlign.Center,
                text = "Официальное приложение сети фотокопицентров «Позитив»",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            Divider()
            ListItem(
                headlineContent = { Text("Подробнее о франшизе") },
                trailingContent = {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW,
                            "https://positivefranchise.ru/".toUri())
                        context.startActivity(intent)
                    }
            )
            Divider()
        }
    }
} 