package net.lds.online.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun MenuIcon(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    tint: Color = Color.Unspecified
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint
        )
    }
} 