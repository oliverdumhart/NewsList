package com.oliverdumhart.moap.newslist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.oliverdumhart.moap.newslist.entities.NewsItem
import java.util.*

@Dao
interface NewsDao {
    @get:Query("SELECT * FROM NewsItem")
    val newsItems : LiveData<List<NewsItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: NewsItem)

    @Query("SELECT EXISTS(SELECT * FROM NewsItem WHERE id = :id)")
    fun exists(id: String) : Boolean

    @Query("DELETE FROM NewsItem WHERE publicationDate < :date")
    fun deleteOldNews(date: Long)
}