package com.pratistha.reminderapp.utils

import com.pratistha.reminderapp.BuildConfig

const val REMINDER="REMINDER"
const val channelId = "reminder_channel_no_vibrate"
const val channelName = "Reminder Notifications"
const val channelDescription = "Channel for medication reminders"

const val ADMOB_BANNER_TEST_ID = "ca-app-pub-3940256099942544/6300978111"
const val ADMOB_BANNER_PROD_ID = "ca-app-pub-2285141709293675/4149623798"

val BANNER_AD_UNIT_ID = if (BuildConfig.DEBUG) ADMOB_BANNER_TEST_ID else ADMOB_BANNER_PROD_ID

val regexForVoice =
    "(?:create|set|add)?\\s*(?:a\\s*)?(?:reminder|alarm)?\\s*(?:for|to)?\\s*(.*?)\\s*(?:tomorrow)?\\s*(?:at)?\\s*(\\d{1,2})(?::(\\d{2}))?\\s*(am|pm|a\\.m\\.|p\\.m\\.)?"
