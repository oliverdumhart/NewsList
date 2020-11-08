package com.oliverdumhart.moap.newslist.services

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.oliverdumhart.moap.newslist.database.NewsRepository
import com.oliverdumhart.moap.newslist.main.MainViewModel
import timber.log.Timber
import java.util.*

class NewsWorkScheduler(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    companion object {
        const val URL_EXTRA: String = "url_extra"
        val WORK_NAME: String = NewsWorkScheduler::class.simpleName!!
    }

    override suspend fun doWork(): Result {

        Timber.i("Start updating news...")

        if (inputData.hasKeyWithValueOfType(URL_EXTRA, String::class.java)) {
            val url = inputData.getString(URL_EXTRA)

            if (url != null) {
                val news = NewsApiService.loadNews(url)

                /*val news2 = news.toMutableList()

                val c = Calendar.getInstance()
                c.set(Calendar.DAY_OF_MONTH, 1)

                val last_idx = news.indexOf(news.last())
                val last = news[last_idx]
                last.publicationDate = c.time
                news2[last_idx] = last*/

                try {
                    val repository = NewsRepository(applicationContext)
                    repository.updateNewsList(news)
                    repository.deleteOldNews()
                } catch (ex: java.lang.Exception) {
                    Timber.e("Error occurred while updating news in database: %s", ex.message)
                    return Result.failure()
                }

            }
        }

        Timber.i("Updating news successful!")
        return Result.success()
    }


}