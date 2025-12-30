package net.online.wvrss.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import net.online.wvrss.model.RssItem
import net.online.wvrss.parser.RssParser
import java.net.URL

class RssRepository {
    fun getRssItems(url: String): Flow<List<RssItem>> = flow {
        try {
            val xml = URL(url).readText()
            val items = RssParser.parse(xml)
            emit(items)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)
} 