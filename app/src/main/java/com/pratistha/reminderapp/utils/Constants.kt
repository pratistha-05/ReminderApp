package com.pratistha.reminderapp.utils

const val REMINDER="REMINDER"
const val channelId = "reminder_channel"
const val channelName = "Reminder Notifications"
const val channelDescription = "Channel for medication reminders"

val regexForVoice = "(?:create|set|add)?\\s*(?:a\\s*)?(?:reminder)?\\s*(?:for|to)?\\s*(.*?)\\s*(?:at)?\\s*(\\d{1,2})(?::(\\d{2}))?\\s*(am|pm|a\\.m\\.|p\\.m\\.)?"