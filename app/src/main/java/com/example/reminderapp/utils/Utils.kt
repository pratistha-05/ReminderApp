package com.example.reminderapp.utils

import java.util.Calendar

fun convertTimeToMillis(time: String): Long {
  val timeParts = time.split(":")
  val hour = timeParts[0].toInt()
  val minute = timeParts[1].toInt()

  val calendar = Calendar.getInstance().apply {
    set(Calendar.HOUR_OF_DAY, hour)
    set(Calendar.MINUTE, minute)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
  }
  return calendar.timeInMillis
}