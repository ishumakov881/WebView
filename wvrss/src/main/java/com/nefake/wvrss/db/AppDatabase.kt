package com.nefake.wvrss.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nefake.wvrss.model.RssItem

@Database(entities = [RssItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteItemsDao(): FavoriteItemsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rss_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 