package com.pratistha.reminderapp.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pratistha.reminderapp.data.local.Reminder
import com.pratistha.reminderapp.presentation.ui.components.DateRowItem
import com.pratistha.reminderapp.presentation.ui.components.EmptyReminderState
import com.pratistha.reminderapp.presentation.ui.components.InputForm
import com.pratistha.reminderapp.presentation.ui.components.ReminderItem
import com.pratistha.reminderapp.presentation.viewmodel.ReminderViewModel
import com.pratistha.reminderapp.utils.alarmSetup
import com.pratistha.reminderapp.utils.convertDateTimeToMillis
import com.pratistha.reminderapp.utils.setUpPeriodicAlarm
import kotlinx.coroutines.launch
import java.time.LocalDate
import com.pratistha.reminderapp.data.local.Frequency
import com.pratistha.reminderapp.utils.cancelAlarm
import com.pratistha.reminderapp.utils.convertMillisToTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ReminderListUi(viewModel: ReminderViewModel = hiltViewModel()) {
    val list = viewModel.list.collectAsState()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val selectedDateStr by viewModel.selectedDate.collectAsState()
    val selectedDate = remember(selectedDateStr) { LocalDate.parse(selectedDateStr) }
    val today = LocalDate.now()
    val editingReminder by viewModel.editingReminder.collectAsState()
    val days = remember {
        (0..6).map { today.plusDays(it.toLong()) }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth(),
        sheetContent = {
            InputForm(
                viewModel = viewModel,
                onTimeClick = { pickedTime ->
                    viewModel.onTimeChange(pickedTime)
                }
            ) {
                val name = viewModel.reminderName.value
                val dosage = viewModel.reminderDosage.value.toString()
                val time = viewModel.reminderTime.value
                val isRepeat = viewModel.isRepeat.value
                val frequency = viewModel.frequency.value

                if (name.isBlank()) {
                    return@InputForm
                }
                if (dosage.isBlank()) {
                    return@InputForm
                }
                if (time.isEmpty()) {
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

                scope.launch {
                    viewModel.clearEditing()
                    sheetState.hide()
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Reminders", color = MaterialTheme.colorScheme.secondary
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = { scope.launch { sheetState.show() } }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Reminder")
                        }
                    }
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        LazyRow(
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(days) { date ->
                                DateRowItem(
                                    selectedDate,
                                    date,
                                    onDateSelect = {
                                        viewModel.selectDate(date.toString())
                                    }
                                )
                            }
                        }
                    }


                    when {
                        list.value.isLoading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        list.value.list.isEmpty() -> {
                            EmptyReminderState()
                        }

                        else -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(list.value.list) { item ->
                                    ReminderItem(item = item, viewModel = viewModel, context) { reminderToEdit ->
                                        viewModel.editReminder(reminderToEdit)
                                        viewModel.selectDate(reminderToEdit.date)
                                        scope.launch { sheetState.show() }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}