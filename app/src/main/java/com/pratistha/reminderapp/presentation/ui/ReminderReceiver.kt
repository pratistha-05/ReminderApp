package com.pratistha.reminderapp.presentation.ui

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.pratistha.reminderapp.R
import com.pratistha.reminderapp.utils.REMINDER
import com.pratistha.reminderapp.data.local.Reminder
import com.pratistha.reminderapp.domain.useCases.UpdateUseCase
import com.pratistha.reminderapp.domain.useCases.InsertReminderUseCase
import com.google.gson.Gson
import com.pratistha.reminderapp.data.local.Frequency
import com.pratistha.reminderapp.domain.repository.ReminderRepository
import com.pratistha.reminderapp.utils.alarm.AlarmService
import com.pratistha.reminderapp.utils.alarm.alarmSetup
import com.pratistha.reminderapp.utils.alarm.cancelAlarm
import com.pratistha.reminderapp.utils.convertDateTimeToMillis
import com.pratistha.reminderapp.utils.convertMillisToTime
import com.pratistha.reminderapp.utils.getDaysToSchedule
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import javax.inject.Inject


@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject lateinit var updateUseCase: UpdateUseCase
    @Inject lateinit var insertUseCase: InsertReminderUseCase
    @Inject lateinit var repository: ReminderRepository

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
                runBlocking { rescheduleIfCycleEnded(context, reminder) }
            }
        }
    }

    private suspend fun rescheduleIfCycleEnded(context: Context, reminder: Reminder) {
        if (!reminder.isRepeat) return
        val lastDate = repository.getLastDateForGroup(reminder.name, reminder.slot, reminder.frequency) ?: return
        if (reminder.date != lastDate) return

        val frequency = Frequency.values().find { it.value == reminder.frequency } ?: return
        val time = convertMillisToTime(reminder.timeinMillis)
        val nextStart = LocalDate.parse(lastDate).plusDays(1)
        val daysToSchedule = getDaysToSchedule(frequency)

        for (i in daysToSchedule) {
            val date = nextStart.plusDays(i.toLong())
            val millis = convertDateTimeToMillis(date, time)
            val newReminder = reminder.copy(id = 0, date = date.toString(), timeinMillis = millis, isTaken = false)
            val newId = insertUseCase.invoke(newReminder)
            alarmSetup(context, newReminder.copy(id = newId.toInt()))
        }
    }
}
