package com.oliverdumhart.moap.dummynewslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliverdumhart.moap.dummynewslist.entities.NewsItem
import kotlinx.coroutines.launch

class NewsListViewModel: ViewModel() {

    private val _newsList = MutableLiveData<List<NewsItem>>()

    val newsList: LiveData<List<NewsItem>>
        get() = _newsList

    fun loadNews(url: String) {
        viewModelScope.launch {
            val result = NewsApiService.loadNews(url)
            _newsList.postValue(result)
        }
    }
}