package com.example.reminderapp.presentation.ui

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.pratistha.reminderapp.R
import com.example.reminderapp.utils.REMINDER
import com.example.reminderapp.data.local.Reminder
import com.example.reminderapp.domain.useCases.UpdateUseCase
import com.example.reminderapp.presentation.AlarmService
import com.example.reminderapp.utils.cancelAlarm
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
        val notificationManager = NotificationManagerCompat.from(context)

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
                context.stopService(Intent(context, AlarmService::class.java))
                notificationManager.cancel(1)
            }
            "REJECT" -> {
                runBlocking {
                    updateUseCase.invoke(reminder.copy(isTaken = false))
                }
                cancelAlarm(context, reminder)
                context.stopService(Intent(context, AlarmService::class.java))
                notificationManager.cancel(1)

            }
            else -> {
                val alarmIntent = Intent(context, AlarmService::class.java).apply {
                    putExtra(REMINDER, reminderJson)
                }
                ContextCompat.startForegroundService(context, alarmIntent)
            }
        }
    }
}
