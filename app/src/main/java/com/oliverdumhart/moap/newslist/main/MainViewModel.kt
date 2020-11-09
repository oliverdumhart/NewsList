package com.oliverdumhart.moap.newslist.main

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.*
import com.oliverdumhart.moap.newslist.database.NewsRepository
import com.oliverdumhart.moap.newslist.services.NewsApiService
import com.oliverdumhart.moap.newslist.entities.NewsItem
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = NewsRepository(application)

    val newsList: LiveData<List<NewsItem>> = repository.newsList
    val recentNews: LiveData<List<NewsItem>> = repository.recentNews

    private val _showImages = MutableLiveData<Boolean>()

    val showImages: LiveData<Boolean>
            get() = _showImages

    enum class Error{
        General, Insert
    }

    fun reloadNews(url: String, callback: (error: Error) -> Unit) {
        viewModelScope.launch {
            val result = NewsApiService.loadNews(url)
            try {
                repository.updateNewsList(result)
            }
            catch (ex: SQLiteConstraintException){
                callback(Error.Insert)
            }
            catch (ex: Exception){
                callback(Error.General)
            }
        }
    }

    fun setShowImages(value: Boolean){
        _showImages.value = value
    }
}