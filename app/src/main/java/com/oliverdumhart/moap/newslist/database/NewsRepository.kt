package com.oliverdumhart.moap.newslist.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.oliverdumhart.moap.newslist.entities.NewsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepository(context: Context) {

    private val newsDao: NewsDao = ApplicationDatabase.getInstance(context).newsDao

    val newsList: LiveData<List<NewsItem>> = newsDao.newsItems

    suspend fun updateNewsList(newsList: List<NewsItem>) {
        withContext(Dispatchers.IO) {
            newsList.forEach {
                newsDao.insert(it)
            }
        }
    }
}