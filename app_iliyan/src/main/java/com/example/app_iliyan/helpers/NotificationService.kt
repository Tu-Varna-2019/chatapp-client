package com.example.app_iliyan.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.app_iliyan.R

class NotificationService {
  companion object {
    fun showNotification(context: Context, title: String, message: String, notificationId: Int) {
      val channelId = "0"
      val channelName = "App Iliyan"
      val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

      val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
      notificationManager.createNotificationChannel(channel)

      val notificationBuilder =
        NotificationCompat.Builder(context, channelId)
          .setContentTitle(title)
          .setContentText(message)
          .setSmallIcon(R.drawable.group)
          .setAutoCancel(true)

      notificationManager.notify(notificationId, notificationBuilder.build())
    }
  }
}
