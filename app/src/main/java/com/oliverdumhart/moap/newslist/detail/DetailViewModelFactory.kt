package com.oliverdumhart.moap.newslist.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oliverdumhart.moap.newslist.entities.NewsItem

class DetailViewModelFactory(
        private val newsItem: NewsItem,
        private val showImages: Boolean
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(newsItem, showImages) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}