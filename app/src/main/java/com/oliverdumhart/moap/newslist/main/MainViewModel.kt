package com.oliverdumhart.moap.newslist.main

import android.app.Application
import androidx.lifecycle.*
import com.oliverdumhart.moap.newslist.database.NewsRepository
import com.oliverdumhart.moap.newslist.services.NewsApiService
import com.oliverdumhart.moap.newslist.entities.NewsItem
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = NewsRepository(application)

    val newsList: LiveData<List<NewsItem>> = repository.newsList

    private val _showImages = MutableLiveData<Boolean>()

    val showImages: LiveData<Boolean>
            get() = _showImages

    fun reloadNews(url: String) {
        viewModelScope.launch {
            val result = NewsApiService.loadNews(url)
            repository.updateNewsList(result)
        }
    }

    fun setShowImages(value: Boolean){
        _showImages.value = value
    }
}