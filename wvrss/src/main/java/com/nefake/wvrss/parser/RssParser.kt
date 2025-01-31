package com.nefake.wvrss.parser

import android.text.Html
import com.nefake.wvrss.model.RssItem
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader

object RssParser {
    fun parse(xml: String): List<RssItem> {
        val items = mutableListOf<RssItem>()
        var currentItem: RssItem? = null
        var currentTag: String? = null
        
        try {
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(StringReader(xml))
            
            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        currentTag = parser.name
                        if (currentTag == "item") {
                            currentItem = RssItem(
                                title = "",
                                link = "",
                                description = "",
                                pubDate = "",
                                imageUrl = null
                            )
                        }
                    }
                    XmlPullParser.TEXT -> {
                        currentItem?.let { item ->
                            val text = parser.text.trim()
                            when (currentTag) {
                                "title" -> item.title = decodeHtml(text)
                                "link" -> item.link = text
                                "description" -> item.description = decodeHtml(text)
                                "pubDate" -> item.pubDate = text
                                "enclosure" -> {
                                    if (parser.getAttributeValue(null, "type")?.startsWith("image/") == true) {
                                        item.imageUrl = parser.getAttributeValue(null, "url")
                                    }
                                }
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "item") {
                            currentItem?.let { items.add(it) }
                            currentItem = null
                        }
                        currentTag = null
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return items
    }
    
    private fun decodeHtml(text: String): String {
        return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString()
    }
} 