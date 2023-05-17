package com.documentstorage.app

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // This method will be called when the alarm is triggered
        // You can schedule and show the local notification here
        showNotification(context)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification(context: Context) {
        val channelId = "your_channel_id"
        val channelName = "Your Channel Name"
        val notificationId = 1

        // Create a notification builder
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_heart_broken_24)
            .setContentTitle("Reminder")
            .setContentText("You haven't entered the app in the last 60 seconds!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        // Create an intent for opening the app when the notification is clicked
        val deepLinkUri = Uri.parse("app://deeplink/main")
        val intent = Intent(Intent.ACTION_VIEW, deepLinkUri)
        intent.setPackage(context.packageName) // Ensure the intent is resolved within your app
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        notificationBuilder.setContentIntent(pendingIntent)

        // Show the notification
        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}