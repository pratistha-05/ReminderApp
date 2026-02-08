package com.pratistha.reminderapp.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pratistha.reminderapp.data.local.Frequency
import com.pratistha.reminderapp.data.local.Reminder
import com.pratistha.reminderapp.presentation.ui.components.InputForm
import com.pratistha.reminderapp.presentation.viewmodel.ReminderViewModel
import com.pratistha.reminderapp.utils.alarmSetup
import com.pratistha.reminderapp.utils.cancelAlarm
import com.pratistha.reminderapp.utils.convertDateTimeToMillis
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderScreen(
    navController: NavController,
    viewModel: ReminderViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val editingReminder by viewModel.editingReminder.collectAsState()
    val selectedDateStr by viewModel.selectedDate.collectAsState()
    val selectedDate = remember(selectedDateStr) { LocalDate.parse(selectedDateStr) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (editingReminder != null) "Edit Reminder" else "Add Reminder",color = MaterialTheme.colorScheme.secondary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
        ) {
            InputForm(
                viewModel = viewModel,
                onTimeClick = { pickedTime ->
                    viewModel.onTimeChange(pickedTime)
                },
                onSaveClick = {
                    val name = viewModel.reminderName.value
                    val dosage = viewModel.reminderDosage.value.toString()
                    val time = viewModel.reminderTime.value
                    val isRepeat = viewModel.isRepeat.value
                    val frequency = viewModel.frequency.value
                    val slot = viewModel.slot.value

                    if (name.isBlank() || dosage.isBlank() || time.isEmpty()) {
                        return@InputForm
                    }

                    val daysToSchedule = if (isRepeat) {
                        when (frequency) {
                            Frequency.Daily -> 0..6
                            Frequency.Alternate -> 0..6 step 2
                            else -> 0..0
                        }
                    } else {
                        0..0
                    }

                    val existing = editingReminder

                    if (existing != null) {
                        // -------- EDIT MODE --------
                        val updatedReminder = existing.copy(
                            name = name,
                            dosage = dosage,
                            slot = slot,
                            timeinMillis = convertDateTimeToMillis(
                                selectedDate,
                                time
                            ),
                            isRepeat = isRepeat,
                            frequency = frequency.value,
                            date = selectedDate.toString()
                        )

                        viewModel.update(updatedReminder)

                        try {
                            cancelAlarm(context, existing)
                            alarmSetup(context, updatedReminder)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        // -------- ADD MODE --------
                        for (i in daysToSchedule) {
                            val dateForReminder = selectedDate.plusDays(i.toLong())
                            val reminderTimeInMillis = convertDateTimeToMillis(
                                dateForReminder,
                                time
                            )

                            val reminder = Reminder(
                                name = name,
                                dosage = dosage,
                                slot = slot,
                                timeinMillis = reminderTimeInMillis,
                                isTaken = false,
                                isRepeat = isRepeat,
                                frequency = frequency.value,
                                date = dateForReminder.toString(),
                            )

                            viewModel.insert(reminder)

                            try {
                                alarmSetup(context, reminder)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    viewModel.clearEditing()
                    navController.popBackStack()
                }
            )
        }
    }
}
