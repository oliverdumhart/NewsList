package com.oliverdumhart.moap.dummynewslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oliverdumhart.moap.dummynewslist.entities.NewsItem

class NewsItemViewModel : ViewModel() {
    private val _newsItem = MutableLiveData<NewsItem>()
    val newsItem: LiveData<NewsItem>
        get() = _newsItem

    fun updateNewsItem(newsItem: NewsItem) {
        _newsItem.value = newsItem
    }

    fun fullStoryButtonClicked() {
        _eventShowLink.value = newsItem.value?.link ?: ""
    }

    private val _eventShowLink = MutableLiveData("")
    val eventShowLink: LiveData<String>
        get() = _eventShowLink

    fun eventShowLinkComplete(){
        _eventShowLink.value = ""
    }
}