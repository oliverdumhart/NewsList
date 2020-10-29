package com.oliverdumhart.moap.newslist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.oliverdumhart.moap.newslist.entities.NewsItem

@Database(entities = [NewsItem::class], version = 1, exportSchema = false)
@TypeConverters(value = [Converters::class])
abstract class ApplicationDatabase : RoomDatabase() {
    abstract val newsDao: NewsDao

    companion object {
        @Volatile
        private var _instance: ApplicationDatabase? = null

        fun getInstance(context: Context): ApplicationDatabase {
            val tmp = _instance

            if (tmp != null) {
                return tmp;
            }

            synchronized(this) {
                var tmp2 = _instance
                if (tmp2 != null) {
                    return tmp2;
                }

                tmp2 = Room.databaseBuilder(context.applicationContext, ApplicationDatabase::class.java, "application_db").build()
                _instance = tmp2
                return tmp2
            }
        }
    }


}