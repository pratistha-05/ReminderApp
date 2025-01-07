package com.example.reminderapp.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.reminderapp.data.local.Reminder
import com.google.gson.Gson

const val REMINDER="REMINDER"

fun alarmSetup(context: Context, reminder: Reminder){
    val intent= Intent(context,ReminderReceiver::class.java).apply{
        putExtra(REMINDER, Gson().toJson(reminder))
    }

    val pendingIntent=PendingIntent.getBroadcast(context,reminder.timeinMillis.toInt(),intent,PendingIntent.FLAG_IMMUTABLE)

    val alarmManager=context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    try{
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,reminder.timeinMillis,pendingIntent)
    }catch (e:SecurityException){
        e.printStackTrace()
    }
}

fun cancelAlarm(context: Context, reminder: Reminder){
    val intent= Intent(context,ReminderReceiver::class.java).apply{
        putExtra(REMINDER, Gson().toJson(reminder))
    }

    val pendingIntent=PendingIntent.getBroadcast(context,reminder.timeinMillis.toInt(),intent,PendingIntent.FLAG_IMMUTABLE)

    val alarmManager=context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    try{
        alarmManager.cancel(pendingIntent)
    }catch (e:SecurityException){
        e.printStackTrace()
    }
}

fun setUpPeriodicAlarm(context: Context, reminder: Reminder){
    val intent= Intent(context,ReminderReceiver::class.java).apply{
        putExtra(REMINDER, Gson().toJson(reminder))
    }

    val pendingIntent=PendingIntent.getBroadcast(context,reminder.timeinMillis.toInt(),intent,PendingIntent.FLAG_IMMUTABLE)

    val alarmManager=context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    try{
        val interval = 2L*60L*1000L
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,reminder.timeinMillis,interval,pendingIntent)
    }catch (e:SecurityException){
        e.printStackTrace()
    }
}