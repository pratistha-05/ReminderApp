package com.pratistha.reminderapp.utils.voice

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.pratistha.reminderapp.data.local.Frequency
import com.pratistha.reminderapp.data.local.Reminder
import com.pratistha.reminderapp.presentation.viewmodel.ReminderViewModel
import com.pratistha.reminderapp.utils.alarm.alarmSetup
import com.pratistha.reminderapp.utils.convertDateTimeToMillis
import java.time.LocalDate

fun startVoiceRecognition(
    launcher: ActivityResultLauncher<Intent>
) {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

    intent.putExtra(
        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
    )

    intent.putExtra(
        RecognizerIntent.EXTRA_PROMPT,
        "Say something like: Set reminder for meds at 8 pm"
    )

    launcher.launch(intent)
}


fun handleVoiceResult(
    resultCode: Int,
    data: Intent?,
    viewModel: ReminderViewModel,
    context: Context,
    call:()->Unit
) {
    if (resultCode != Activity.RESULT_OK) return

    val matches = data?.getStringArrayListExtra(
        RecognizerIntent.EXTRA_RESULTS
    )

    val spokenText = matches?.get(0) ?: return

    val normalizedText = spokenText
        .lowercase()
        .replace("p m", "pm")
        .replace("a m", "am")
        .replace("p.m.", "pm")
        .replace("a.m.", "am")
        .replace("pm.", "pm")
        .replace("am.", "am")

    val parsed = VoiceReminderParser.parse(normalizedText) ?: return

    var date = LocalDate.now()

    if (parsed.isTomorrow) {
        date = date.plusDays(1)
    }

    var reminderMillis = convertDateTimeToMillis(date, parsed.time)

    val now = System.currentTimeMillis()

    // if not tomorrow but time already passed → shift to tomorrow
    if (!parsed.isTomorrow && reminderMillis < now) {
//        date = date.plusDays(1)
//        reminderMillis = convertDateTimeToMillis(date, parsed.time)

        return Toast.makeText(
            context,
            "Time already passed today. Reminder set for tomorrow.",
            Toast.LENGTH_LONG
        ).show()
    }

    val reminder = Reminder(
        name = parsed.title,
        dosage = "1",
        slot = "",
        timeinMillis = reminderMillis,
        isTaken = false,
        isRepeat = false,
        frequency = Frequency.Once.value,
        date = date.toString()
    )

    viewModel.insert(reminder)

    try {
        alarmSetup(context, reminder)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
/*
1. if im adding 8pm, 8am is getting added(24-hr format), its not parsing pm
2. tomorrow needs to be set
3. past time should not be set - show toast
 */