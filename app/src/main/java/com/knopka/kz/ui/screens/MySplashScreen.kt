package com.knopka.kz.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.knopka.kz.BuildConfig
import com.knopka.kz.R


@Preview
@Composable
fun MySplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface), // Фон сплеш-экрана

        contentAlignment = Alignment.Center
    ) {


//        Image(
//            painter = painterResource(id = R.drawable.bg), // Замените на свой PNG
//            contentDescription = null,
//            contentScale = ContentScale.Crop, // Заполняет всю область
//            modifier = Modifier.matchParentSize() // Растягивает изображение на весь экран
//        )


        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // 1. Иконка приложения (круглая)
            Image(
                painter = painterResource(id = R.drawable.app_icon), // Замените на вашу иконку
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape) // Делаем иконку круглой
                    .background(Color.LightGray.copy(alpha = 0.3f)) // Опционально: добавляем фон
            )

            // 2. Название программы
            Text(
                text = stringResource(id = R.string.app_name), // Название программы из strings.xml
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            // 3. Версия приложения
            Text(
                text = "Version ${BuildConfig.VERSION_NAME}", // Версия из BuildConfig
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}