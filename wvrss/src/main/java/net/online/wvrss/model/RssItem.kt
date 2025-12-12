package net.online.wvrss.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_items")
data class RssItem(
    @PrimaryKey
    public var link: String,
    var title: String,
    var description: String,
    var pubDate: String,
    var imageUrl: String? = null,
    var isFavorite: Boolean = false
) 