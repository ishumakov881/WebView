package com.nefake.wvrss.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nefake.wvrss.model.RssItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RssItemCard(
    item: RssItem,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) Color.DarkGray else Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isDarkTheme) Color.White else Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = item.pubDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isDarkTheme) Color.LightGray else Color.Gray
                )
            }
            
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (item.isFavorite) "Удалить из избранного" else "Добавить в избранное",
                    tint = if (item.isFavorite) Color.Red else if (isDarkTheme) Color.White else Color.Black
                )
            }
        }
    }
} 