package com.oliverdumhart.moap.newslist.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliverdumhart.moap.newslist.services.NewsApiService
import com.oliverdumhart.moap.newslist.entities.NewsItem
import kotlinx.coroutines.launch

class NewsListViewModel: ViewModel() {

    private val _newsList = MutableLiveData<List<NewsItem>>()

    val newsList: LiveData<List<NewsItem>>
        get() = _newsList

    private val _showImages = MutableLiveData<Boolean>()

    val showImages: LiveData<Boolean>
            get() = _showImages

    fun loadNews(url: String) {
        viewModelScope.launch {
            val result = NewsApiService.loadNews(url)
            _newsList.postValue(result)
        }
    }

    fun setShowImages(value: Boolean){
        _showImages.value = value
    }
}