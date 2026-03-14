package com.pratistha.reminderapp.utils

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.result.ActivityResultLauncher
import com.pratistha.reminderapp.data.local.Frequency
import com.pratistha.reminderapp.data.local.Reminder
import com.pratistha.reminderapp.presentation.viewmodel.ReminderViewModel
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
        "Say something like: Create reminder for meds at 8 pm"
    )

    launcher.launch(intent)
}


fun handleVoiceResult(
    resultCode: Int,
    data: Intent?,
    viewModel: ReminderViewModel
) {
    if (resultCode != Activity.RESULT_OK) return

    val matches = data?.getStringArrayListExtra(
        RecognizerIntent.EXTRA_RESULTS
    )

    val spokenText = matches?.get(0) ?: return

    val parsed = VoiceReminderParser.parse(spokenText)

    if (parsed != null) {

        val (title, time) = parsed
        val date = LocalDate.now()

        val reminder = Reminder(
            name = title,
            dosage = "1",
            slot = "",
            timeinMillis = convertDateTimeToMillis(date, time),
            isTaken = false,
            isRepeat = false,
            frequency = Frequency.Once.value,
            date = date.toString()
        )

        viewModel.insert(reminder)
    }
}
