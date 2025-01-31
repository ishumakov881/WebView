package com.nefake.wvrss.repository

import com.nefake.wvrss.db.FavoriteItemsDao
import com.nefake.wvrss.model.RssItem
import kotlinx.coroutines.flow.Flow

class FavoritesRepository(private val favoriteItemsDao: FavoriteItemsDao) {
    fun getAllFavorites(): Flow<List<RssItem>> = favoriteItemsDao.getAllFavorites()
    
    suspend fun toggleFavorite(item: RssItem) {
        if (favoriteItemsDao.isFavorite(item.link)) {
            favoriteItemsDao.removeFromFavorites(item)
        } else {
            item.isFavorite = true
            favoriteItemsDao.addToFavorites(item)
        }
    }
    
    suspend fun isFavorite(link: String): Boolean = favoriteItemsDao.isFavorite(link)
} 