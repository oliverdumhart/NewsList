package com.oliverdumhart.moap.newslist

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.preference.PreferenceManager
import com.oliverdumhart.moap.newslist.detail.DetailActivity
import com.oliverdumhart.moap.newslist.entities.NewsItem
import com.oliverdumhart.moap.newslist.main.MainActivity

object NotificationUtils {

    private const val NOTIFICATION_ID_KEY = "notification_id"
    private const val CHANNEL_ID = "channel_id"

    fun setupNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun createNotification(context: Context, newsItem: NewsItem) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        var notificationId = sharedPreferences.getInt(NOTIFICATION_ID_KEY, 0)
        notificationId++
        sharedPreferences.edit().putInt(NOTIFICATION_ID_KEY, notificationId).apply()

        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, CHANNEL_ID)
        } else {
            NotificationCompat.Builder(context)
        }
        builder.setSmallIcon(R.drawable.ic_baseline_message_24)
                .setContentTitle(newsItem.title)
                .setContentText(newsItem.description)
                .setAutoCancel(true)

        // Intent that starts when clicking on the notification
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(DetailActivity.ITEM_EXTRA, newsItem)

        val taskStackBuilder = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(intent)

        val pendingIntent = taskStackBuilder.getPendingIntent(notificationId,
                PendingIntent.FLAG_UPDATE_CURRENT)

        builder.setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.notify(notificationId, builder.build())

    }
}