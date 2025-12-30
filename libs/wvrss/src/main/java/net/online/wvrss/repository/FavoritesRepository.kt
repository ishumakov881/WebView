package net.online.wvrss.repository

import kotlinx.coroutines.flow.Flow
import net.online.wvrss.db.FavoriteItemsDao
import net.online.wvrss.model.RssItem

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