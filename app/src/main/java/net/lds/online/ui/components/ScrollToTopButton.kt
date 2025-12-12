package net.lds.online.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ScrollToTopButton(
    isVisible: Boolean,
    onClick: () -> Unit,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
        modifier = modifier.padding(16.dp)
    ) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = if (isDarkTheme) Color.White else Color.Black,
            contentColor = if (isDarkTheme) Color.Black else Color.White
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Прокрутить вверх"
            )
        }
    }
} 