//package com.nefake.ui.components
//
//import androidx.compose.animation.core.*
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.nefake.core.Constants
//
//@Composable
//fun LoadingAnimation(
//    modifier: Modifier = Modifier
//) {
//    val transition = rememberInfiniteTransition()
//    val dots = listOf(0, 1, 2).map { index ->
//        val delay = index * (Constants.Animation.LOADING_ANIMATION_DURATION / 3)
//        val scale by transition.animateFloat(
//            initialValue = 0.2f,
//            targetValue = 1f,
//            animationSpec = infiniteRepeatable(
//                animation = tween(
//                    durationMillis = Constants.Animation.LOADING_ANIMATION_DURATION,
//                    delayMillis = delay,
//                    easing = FastOutSlowInEasing
//                ),
//                repeatMode = RepeatMode.Reverse
//            )
//        )
//        scale
//    }
//
//    Row(
//        modifier = modifier,
//        horizontalArrangement = Arrangement.spacedBy(4.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        dots.forEach { scale ->
//            Box(
//                modifier = Modifier
//                    .size(8.dp * scale.toFloat())
//                    .background(
//                        color = MaterialTheme.colorScheme.primary,
//                        shape = CircleShape
//                    )
//            )
//        }
//    }
//}