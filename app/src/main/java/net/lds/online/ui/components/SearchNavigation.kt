package net.lds.online.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SearchNavigation(
    currentMatch: Int,
    totalMatches: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    if (totalMatches > 0) {
        Row(
            modifier = modifier
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(
                onClick = onPrevious,
                enabled = totalMatches > 1
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Предыдущее совпадение",
                    tint = if (isDarkTheme) Color.White else Color.Black
                )
            }
            
            Text(
                text = "${currentMatch + 1}/$totalMatches",
                style = MaterialTheme.typography.bodySmall,
                color = if (isDarkTheme) Color.White else Color.Black
            )
            
            IconButton(
                onClick = onNext,
                enabled = totalMatches > 1
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Следующее совпадение",
                    tint = if (isDarkTheme) Color.White else Color.Black
                )
            }
        }
    }
} 