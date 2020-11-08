package com.oliverdumhart.moap.newslist.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oliverdumhart.moap.newslist.entities.NewsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepository(context: Context) {

    companion object {
        private const val FIVE_DAYS = 5 * 24 * 60 * 60 * 1_000
    }

    private val newsDao: NewsDao = ApplicationDatabase.getInstance(context).newsDao

    val newsList: LiveData<List<NewsItem>> = newsDao.newsItems
    private val _recentNews = MutableLiveData<List<NewsItem>>()
    val recentNews: LiveData<List<NewsItem>>
        get() = _recentNews

    suspend fun updateNewsList(newsList: List<NewsItem>) {
        withContext(Dispatchers.IO) {
            val newItems = mutableListOf<NewsItem>()
            newsList.forEach {
                if (!newsDao.exists(it.id)) {
                    newsDao.insert(it).toString()
                    newItems.add(it)
                }
            }
            withContext(Dispatchers.Main) {
                _recentNews.value = newItems
            }
        }
    }

    suspend fun deleteOldNews() = withContext(Dispatchers.IO) {
        val millis = System.currentTimeMillis() - FIVE_DAYS
        newsDao.deleteOldNews(millis)
    }
}