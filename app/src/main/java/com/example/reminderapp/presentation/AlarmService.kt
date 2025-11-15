package com.example.reminderapp.presentation

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.reminderapp.R
import com.example.reminderapp.data.local.Reminder
import com.example.reminderapp.presentation.ui.ReminderReceiver
import com.example.reminderapp.utils.REMINDER
import com.example.reminderapp.utils.channelId
import com.google.gson.Gson

class AlarmService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var shakeDetector: ShakeDetector
    private lateinit var reminder: Reminder

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.music)
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
        startForeground(1, buildNotification(reminder))

        mediaPlayer.isLooping = true
        mediaPlayer.start()
        shakeDetector.start()

        return START_STICKY
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        mediaPlayer.release()
        shakeDetector.stop()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun buildNotification(reminder: Reminder): Notification {
        val doneIntent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra(REMINDER, Gson().toJson(reminder))
            action = "DONE"
        }
        val donePendingIntent = PendingIntent.getBroadcast(
            this,
            reminder.timeinMillis.toInt(),
            doneIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val rejectIntent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra(REMINDER, Gson().toJson(reminder))
            action = "REJECT"
        }
        val rejectPendingIntent = PendingIntent.getBroadcast(
            this,
            reminder.timeinMillis.toInt(),
            rejectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Medication reminder")
            .setContentText("Take ${reminder.name} with dosage ${reminder.dosage}")
            .addAction(R.drawable.ic_launcher_foreground, "Done", donePendingIntent)
            .addAction(R.drawable.ic_launcher_foreground, "Close", rejectPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()
    }
}
