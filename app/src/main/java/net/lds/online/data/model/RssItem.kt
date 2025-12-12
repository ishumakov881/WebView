package net.lds.online.data.model

data class RssItem(
    val title: String,
    val link: String,
    val description: String,
    val pubDate: String,
    val imageUrl: String? = null
) 