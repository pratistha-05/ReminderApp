package com.example.reminderapp.presentation

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.reminderapp.R
import com.example.reminderapp.data.local.Reminder
import com.example.reminderapp.domain.useCases.UpdateUseCase
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject lateinit var updateUseCase: UpdateUseCase

    override fun onReceive(context: Context, intent: Intent) {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.music)
        Log.d("ReminderApp", "Received reminder: ${intent.getStringExtra(REMINDER)}")
        val reminderJson = intent.getStringExtra(REMINDER)
        val reminder = Gson().fromJson(reminderJson, Reminder::class.java)

        val doneIntent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(REMINDER, reminderJson)
            action = "DONE"
        }
        val donePendingIntent = PendingIntent.getBroadcast(context, reminder.timeinMillis.toInt(), doneIntent, PendingIntent.FLAG_IMMUTABLE)

        val rejectIntent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(REMINDER, reminderJson)
            action = "REJECT"
        }
        val rejectPendingIntent = PendingIntent.getBroadcast(context, reminder.timeinMillis.toInt(), rejectIntent, PendingIntent.FLAG_IMMUTABLE)

        when (intent.action) {
            "DONE" -> {
                runBlocking {
                    updateUseCase.invoke(reminder.copy(isTaken = true))
                }
                cancelAlarm(context, reminder)
                mediaPlayer.stop()
            }
            "REJECT" -> {
                runBlocking {
                    updateUseCase.invoke(reminder.copy(isTaken = false))
                }
                cancelAlarm(context, reminder)
                mediaPlayer.stop()
            }
            else -> {
                        val notification = NotificationCompat.Builder(context,"reminder_channel")
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle("Medication reminder")
                            .setContentText("Take ${reminder.name} with dosage ${reminder.dosage}")
                            .addAction(R.drawable.ic_launcher_foreground, "Done", donePendingIntent)
                            .addAction(R.drawable.ic_launcher_foreground, "Close", rejectPendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .build()

                        NotificationManagerCompat.from(context).notify(1, notification)

                mediaPlayer.start()
            }
        }
    }
}
