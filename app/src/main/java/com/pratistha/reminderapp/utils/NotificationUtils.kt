package com.pratistha.reminderapp.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.pratistha.reminderapp.R

fun showLowStockNotification(context: Context, medicineName: String) {
    val notificationManager = NotificationManagerCompat.from(context)
    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Medicine Out of Stock")
        .setContentText("Your medicine $medicineName is out of stock. Please refill soon.")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        notificationManager.notify(medicineName.hashCode(), notification)
    }
}