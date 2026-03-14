package com.pratistha.reminderapp.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.pratistha.reminderapp.data.local.Frequency
import com.pratistha.reminderapp.data.local.Reminder
import com.pratistha.reminderapp.presentation.viewmodel.ReminderViewModel
import java.time.LocalDate

fun startVoiceRecognition(context: Context, viewModel: ReminderViewModel) {

    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

    intent.putExtra(
        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
    )

    intent.putExtra(
        RecognizerIntent.EXTRA_PROMPT,
        "Say something like 'Create reminder for medicine at 8 pm'"
    )

    val recognizer = SpeechRecognizer.createSpeechRecognizer(context)

    recognizer.setRecognitionListener(object : RecognitionListener {

        override fun onResults(results: Bundle) {

            val data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

            val spokenText = data?.get(0) ?: return

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
                    frequency = Frequency.Daily.value,
                    date = date.toString()
                )

                viewModel.insert(reminder)
            }
        }

        override fun onError(error: Int) {}
        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
    })

    recognizer.startListening(intent)
}
