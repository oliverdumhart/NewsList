package com.oliverdumhart.moap.newslist.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oliverdumhart.moap.newslist.entities.NewsItem

class DetailViewModel(val newsItem: NewsItem, val showImages: Boolean) : ViewModel() {

    fun fullStoryButtonClicked() {
        _eventShowLink.value = newsItem.link ?: ""
    }

    private val _eventShowLink = MutableLiveData("")
    val eventShowLink: LiveData<String>
        get() = _eventShowLink

    fun eventShowLinkComplete() {
        _eventShowLink.value = ""
    }
}