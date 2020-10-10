package com.oliverdumhart.moap.dummynewslist

import android.util.Xml
import com.oliverdumhart.moap.dummynewslist.entities.NewsItem
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class NewsXmlParser {

    private val ns: String? = null

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): List<NewsItem> {
        inputStream.use { inStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inStream, null)
            parser.nextTag()
            parser.nextTag()
            return readChannel(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readChannel(parser: XmlPullParser): List<NewsItem> {
        val entries = mutableListOf<NewsItem>()

        parser.require(XmlPullParser.START_TAG, ns, "channel")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            // Starts by looking for the item tag
            if (parser.name == "item") {
                entries.add(readItem(parser))
            } else {
                skip(parser)
            }
        }
        return entries
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readItem(parser: XmlPullParser): NewsItem {
        parser.require(XmlPullParser.START_TAG, ns, "item")
        var title: String? = null
        var description: String? = null
        var image: String? = null
        var link: String? = null
        var id: String? = null
        var author: String? = null
        var pubDate: Date? = null
        val categories = mutableSetOf<String>()
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "title" -> title = readTextOfTag("title", parser)
                "description" -> {
                    val text = readTextOfTag("description", parser)
                    val values = Regex("<img src=\"(.*)\" />(.*)").find(text)?.groupValues ?: listOf<String>()
                    if(values.size >= 2) {
                        image = values[1]
                    }
                    if(values.size >= 3) {
                        description = values[2]
                    }
                }
                "link" -> link = readTextOfTag("link", parser)
                "category" -> categories.add(readTextOfTag("category", parser))
                "dc:creator" -> author = readTextOfTag("dc:creator", parser)
                "pubDate" -> pubDate = readDate(parser)
                "dc:identifier" -> id = readTextOfTag("dc:identifier", parser)
                else -> skip(parser)
            }
        }
        return NewsItem(id, title, description, link, image, pubDate, author, categories)
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readTextOfTag(tagName: String, parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, tagName)
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, tagName)
        return title
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readDate(parser: XmlPullParser): Date? {
        val dateString = readTextOfTag("pubDate", parser)
        val sdf = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z")
        return sdf.parse(dateString)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

}
