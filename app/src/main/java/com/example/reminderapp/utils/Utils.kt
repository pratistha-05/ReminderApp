package com.example.reminderapp.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun convertDateTimeToMillis(
    date: LocalDate,
    time: String
): Long {
    val (hour, minute) = time.split(":").map { it.toInt() }

    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, date.year)
        set(Calendar.MONTH, date.monthValue - 1) // Calendar is 0-based
        set(Calendar.DAY_OF_MONTH, date.dayOfMonth)
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    return calendar.timeInMillis
}


fun convertMillisToTime(timeInMillis: Long): String {
  val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
  return formatter.format(Date(timeInMillis))
}
