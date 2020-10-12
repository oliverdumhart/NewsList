package com.oliverdumhart.moap.newslist.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oliverdumhart.moap.newslist.entities.NewsItem

class NewsItemViewModelFactory(
        private val newsItem: NewsItem,
        private val showImages: Boolean
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsItemViewModel::class.java)) {
            return NewsItemViewModel(newsItem, showImages) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}