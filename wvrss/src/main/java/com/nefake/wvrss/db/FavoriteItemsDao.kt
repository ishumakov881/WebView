package com.nefake.wvrss.db

import androidx.room.*
import com.nefake.wvrss.model.RssItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteItemsDao {
    @Query("SELECT * FROM favorite_items")
    fun getAllFavorites(): Flow<List<RssItem>>
    
    @Query("SELECT * FROM favorite_items WHERE link = :link")
    suspend fun getFavoriteByLink(link: String): RssItem?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(item: RssItem)
    
    @Delete
    suspend fun removeFromFavorites(item: RssItem)
    
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_items WHERE link = :link)")
    suspend fun isFavorite(link: String): Boolean
} 