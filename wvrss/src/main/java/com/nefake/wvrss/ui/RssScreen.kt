package com.nefake.wvrss.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nefake.wvrss.db.AppDatabase
import com.nefake.wvrss.model.RssItem
import com.nefake.wvrss.repository.FavoritesRepository
import com.nefake.wvrss.repository.RssRepository
import kotlinx.coroutines.flow.map

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RssScreen(
    url: String,
    onItemClick: (RssItem) -> Unit,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { RssRepository() }
    val favoritesRepository = remember { 
        FavoritesRepository(AppDatabase.getDatabase(context).favoriteItemsDao())
    }
    
    var items by remember { mutableStateOf<List<RssItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    
    // Загружаем RSS ленту
    LaunchedEffect(url) {
        try {
//            isLoading = true
//            error = null
//            val rssItems = repository.getRssItems(url)
//
//            // Проверяем статус избранного для каждого элемента
//            val updatedItems = rssItems.map { item1 ->
//                val isFavorite = favoritesRepository.isFavorite(item1.link)
//                item1.copy(isFavorite = isFavorite)
//            }
//            items = updatedItems
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoading = false
        }
    }
    
    // Отслеживаем изменения в избранном
    val favorites by favoritesRepository.getAllFavorites().collectAsState(initial = emptyList())
    
    LaunchedEffect(favorites) {
        items = items.map { item ->
            item.copy(isFavorite = favorites.any { it.link == item.link })
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("RSS лента") },
                navigationIcon = {
                    IconButton(onClick = { /* Обработка кнопки назад */ }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад",
                            tint = if (isDarkTheme) Color.White else Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = if (isDarkTheme) Color.White else Color.Black
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
                )
            } else if (error != null) {
                Text(
                    text = "Ошибка загрузки: $error",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items) { item ->
                        RssItemCard(
                            item = item,
                            onClick = { onItemClick(item) },
//                            onFavoriteClick = {
//                                scope.launch {
//                                    favoritesRepository.toggleFavorite(item)
//                                }
//                            },
                            isDarkTheme = isDarkTheme
                        )
                    }
                }
            }
        }
    }
} 