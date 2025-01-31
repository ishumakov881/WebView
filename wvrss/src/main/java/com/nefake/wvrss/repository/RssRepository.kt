package com.nefake.wvrss.repository

import com.nefake.wvrss.model.RssItem
import com.nefake.wvrss.parser.RssParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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