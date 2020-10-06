package com.oliverdumhart.moap.dummynewslist

import android.util.Log
import com.oliverdumhart.moap.dummynewslist.entities.NewsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class NewsApiService {
    companion object {
        private val LOG_TAG = NewsApiService::class.java.toString()

        suspend fun loadNews(url: String) : List<NewsItem> {
            var result: List<NewsItem>? = null
            withContext(Dispatchers.IO) {
                Log.i(LOG_TAG, "Start loading data from $url")
                var connection: HttpURLConnection? = null
                try {
                    connection = URL(url).openConnection() as? HttpURLConnection
                    connection?.run {
                        readTimeout = 10000
                        connectTimeout = 15000
                        requestMethod = "GET"
                        doInput = true
                        // Starts the query
                        connect()
                        inputStream
                    }?.use {
                        result = parseNewsItemsFromXml(it)
                        Log.i(LOG_TAG, "Loading and parsing finished")
                    }
                } catch (ex: FileNotFoundException) {
                    Log.e(LOG_TAG, "Data from $url could not be loaded!")
                }finally {
                    connection?.disconnect()
                }
            }
            return result ?: listOf()
        }

        private suspend fun parseNewsItemsFromXml(stream: InputStream) : List<NewsItem> {
            var result: List<NewsItem>? = null
            withContext(Dispatchers.Default) {
                try {
                    result = NewsXmlParser().parse(stream)
                } catch (e: Exception) {
                    Log.e(LOG_TAG, e.message ?: "")
                }
            }
            return result ?: listOf()
        }


    }
}