package com.youcef_bounaas.cibo.features.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Icon
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.youcef_bounaas.cibo.R




fun showLocalNotification(context: Context) {
    // Create the Notification Channel (Android 8.0+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "Reminders notification"
        val channel = NotificationChannel(
            channelId,
            "Reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Reminder notifications for grinding!"
        }

        // Register the channel with the system
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // Create the notification
    val builder = NotificationCompat.Builder(context, "Reminders notification")
        .setSmallIcon(R.drawable.notification_bell)
        .setContentTitle("Grinding Reminder")
        .setContentText("The warrior is back with victory and glory!")
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    // Show the notification
    val notificationManagerCompat = NotificationManagerCompat.from(context)
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Permission not granted, log or handle this case
        Log.e("Notification", "Permission not granted for POST_NOTIFICATIONS")
        return
    }
    notificationManagerCompat.notify(1, builder.build())
}
