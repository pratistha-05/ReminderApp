package com.pratistha.reminderapp.utils.alarm

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.pratistha.reminderapp.MainActivity
import com.pratistha.reminderapp.R
import com.pratistha.reminderapp.data.local.Reminder
import com.pratistha.reminderapp.presentation.ShakeDetector
import com.pratistha.reminderapp.utils.ReminderReceiver
import com.pratistha.reminderapp.utils.REMINDER
import com.pratistha.reminderapp.utils.channelId

class AlarmService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var shakeDetector: ShakeDetector
    private lateinit var reminder: Reminder

    private val handler = Handler(Looper.getMainLooper())
    private val stopServiceRunnable = Runnable {
        // Detach notification and stop service to save battery
        stopForeground(STOP_FOREGROUND_DETACH)
        stopSelf()
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.ringtone)
        shakeDetector = ShakeDetector(this) {
            stopSelf()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val reminderJson = intent?.getStringExtra(REMINDER)
        if (reminderJson == null) {
            // No reminder data, stop service
            stopSelf()
            return START_NOT_STICKY
        }
        reminder = Gson().fromJson(reminderJson, Reminder::class.java)
        val notificationId = reminder.id + 100 // Use a unique ID based on reminder
        startForeground(notificationId, buildNotification(reminder))

        mediaPlayer.isLooping = true
        mediaPlayer.start()
        shakeDetector.start()

        // Auto-stop service after 60 seconds to minimize background activity
        // The notification will remain in the tray due to STOP_FOREGROUND_DETACH
        handler.removeCallbacks(stopServiceRunnable)
        handler.postDelayed(stopServiceRunnable, 30000)

        return START_STICKY
    }

    override fun onDestroy() {
        handler.removeCallbacks(stopServiceRunnable)
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        if (::shakeDetector.isInitialized) {
            shakeDetector.stop()
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun buildNotification(reminder: Reminder): Notification {
        val doneIntent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra(REMINDER, Gson().toJson(reminder))
            action = "DONE"
        }
        val donePendingIntent = PendingIntent.getBroadcast(
            this,
            reminder.id,
            doneIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val rejectIntent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra(REMINDER, Gson().toJson(reminder))
            action = "REJECT"
        }
        val rejectPendingIntent = PendingIntent.getBroadcast(
            this,
            reminder.id + 1000, // Different request code
            rejectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Medication reminder")
            .setContentText("Take ${reminder.name} with dosage ${reminder.dosage}")
            .addAction(R.drawable.ic_launcher_foreground, "Taken", donePendingIntent)
            .addAction(R.drawable.ic_launcher_foreground, "Skip", rejectPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .build()
    }
}
