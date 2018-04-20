package com.android.enmycity.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.android.enmycity.main.UserMainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationService : FirebaseMessagingService() {
  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    super.onMessageReceived(remoteMessage)
    remoteMessage.notification?.let {
      showNotification(it.body ?: "")
    }
  }

  private fun showNotification(body: String) {
    val intent = Intent(this, UserMainActivity::class.java)
    intent.putExtra("hola", 1)

    val contIntent = PendingIntent.getActivity(this, 123, intent, PendingIntent.FLAG_ONE_SHOT)
    val notification = NotificationCompat.Builder(this, "2")
        .setSmallIcon(android.R.drawable.ic_notification_overlay)
        .setContentTitle(body)
        .setContentText("K ases? ajaja")
        .setContentIntent(contIntent)
        .build()

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(0, notification)
  }
}