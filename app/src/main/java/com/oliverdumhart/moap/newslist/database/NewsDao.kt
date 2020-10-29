package com.oliverdumhart.moap.newslist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.oliverdumhart.moap.newslist.entities.NewsItem

@Dao
interface NewsDao {
    @get:Query("SELECT * FROM NewsItem")
    val newsItems : LiveData<List<NewsItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: NewsItem)
}