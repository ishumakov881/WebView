package com.walhalla.nointernet

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun NoInternetSignalAnimation(
    modifier: Modifier = Modifier,
    isAirplaneModeOn: Boolean = false // Установите true для анимации самолета
) {
    BoxWithConstraints(
        modifier = modifier.size(200.dp), // Размер как в оригинальном layout
        contentAlignment = Alignment.Companion.Center
    ) {
        val maxWidth = this.maxWidth

        // 1. Анимация пульсирующих кругов
        SignalCircle(startDelay = 0)
        SignalCircle(startDelay = 600)
        SignalCircle(startDelay = 1200)

        // 2. Статичная центральная иконка
        Image(
            painter = painterResource(id = R.drawable.ic_no_internet_no_bg),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
        )

        // 3. Анимация облаков
        Cloud(parentWidth = maxWidth, iconRes = R.drawable.ic_cloud_1)
        Cloud(parentWidth = maxWidth, iconRes = R.drawable.ic_cloud_2)
        Cloud(parentWidth = maxWidth, iconRes = R.drawable.ic_cloud_3)

        // 4. Анимация самолета (если нужно)
        if (isAirplaneModeOn) {
            Airplane(parentWidth = maxWidth)
        }
    }
}
// --- Вспомогательные Composable для анимаций ---

@Composable
private fun SignalCircle(startDelay: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "signal_circle")

    val animationValue by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 5000,
                delayMillis = startDelay, // Задержка для каждого круга
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ), label = "signal_value"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = animationValue
                scaleY = animationValue
                alpha = 1 - animationValue
            }
            .clip(CircleShape)
            // В оригинале используется drawable, здесь для простоты цвет
            .background(Color(0xFFFFDB98).copy(alpha = 0.5f))
    )
}

@Composable
private fun Cloud(parentWidth: Dp, iconRes: Int) {
    // Используем `remember` для генерации случайных значений только один раз
    val randomDuration = remember { Random.nextLong(8000, 16000) }
    val randomStartDelay = remember { Random.nextInt(0, 5000) }
    val randomYOffset = remember { Random.nextInt(-20, 20).dp }

    val infiniteTransition = rememberInfiniteTransition(label = "cloud")

    val translationX by infiniteTransition.animateValue(
        initialValue = parentWidth * 0.6f,
        targetValue = -parentWidth * 0.6f,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = randomDuration.toInt(),
                delayMillis = randomStartDelay,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ), label = "cloud_translation"
    )

    Image(
        painter = painterResource(id = iconRes),
        contentDescription = null,
        modifier = Modifier
            .offset(x = translationX, y = randomYOffset)
            .size(32.dp)
    )
}

@Composable
private fun Airplane(parentWidth: Dp) {
    val infiniteTransition = rememberInfiniteTransition(label = "airplane")

    // Анимация перемещения
    val translationX by infiniteTransition.animateValue(
        initialValue = -parentWidth * 0.6f,
        targetValue = parentWidth * 0.6f,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ), label = "airplane_translation"
    )

    // Анимация масштаба и прозрачности (у них одна длительность)
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000),
            repeatMode = RepeatMode.Reverse
        ), label = "airplane_pulse"
    )

    Image(
        painter = painterResource(id = R.drawable.ic_airplane),
        contentDescription = null,
        modifier = Modifier
            .size(24.dp)
            .offset(x = translationX)
            .graphicsLayer {
                rotationZ = 90f // Поворот как в оригинале
                scaleX = pulse
                scaleY = pulse
                alpha = pulse // Alpha меняется вместе с масштабом
            }
    )
}


// --- Preview для демонстрации ---

@Preview(showBackground = true)
@Composable
private fun NoInternetSignalAnimation_NoAirplanePreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        NoInternetSignalAnimation(isAirplaneModeOn = false)
    }
}

@Preview(showBackground = true)
@Composable
private fun NoInternetSignalAnimation_WithAirplanePreview() {
    var isAirplaneMode by remember { mutableStateOf(false) }

    // Используем LaunchedEffect для отложенного включения
    LaunchedEffect(Unit) {
        delay(500)
        isAirplaneMode = true
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        NoInternetSignalAnimation(isAirplaneModeOn = isAirplaneMode)
    }
}