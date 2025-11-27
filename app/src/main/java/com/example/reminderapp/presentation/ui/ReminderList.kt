package com.example.reminderapp.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.reminderapp.data.local.Reminder
import com.example.reminderapp.presentation.ui.components.DateRowItem
import com.example.reminderapp.presentation.ui.components.EmptyReminderState
import com.example.reminderapp.presentation.ui.components.InputForm
import com.example.reminderapp.presentation.ui.components.ReminderItem
import com.example.reminderapp.presentation.viewmodel.ReminderViewModel
import com.example.reminderapp.utils.alarmSetup
import com.example.reminderapp.utils.convertTimeToMillis
import com.example.reminderapp.utils.setUpPeriodicAlarm
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ReminderListUi(viewModel: ReminderViewModel = hiltViewModel()) {
    val list = viewModel.list.collectAsState()

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var selectedTime = remember { mutableStateOf("") }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val today = LocalDate.now()
    val days = remember {
        (0..6).map { today.plusDays(it.toLong()) }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth(),
        sheetContent = {
            InputForm(
                time = selectedTime.value,
                onTimeClick = { pickedTime ->
                    selectedTime.value = pickedTime
                }
            ) { name, dosage, isRepeat, intervalTime ->
                //TODO: validation
                val reminder = Reminder(
                    name = name,
                    dosage = dosage,
                    timeinMillis = convertTimeToMillis(selectedTime.value),
                    isTaken = false,
                    isRepeat = isRepeat,
                    date = selectedDate.value.toString(),
                )
                viewModel.insert(reminder)
                if (isRepeat && intervalTime > 0)
                    setUpPeriodicAlarm(context, reminder, intervalTime)
                else
                    alarmSetup(context, reminder)
                scope.launch {
                    selectedTime.value = ""
                    sheetState.hide()
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Reminders", color = Color.Black) },
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
                    LazyRow(
                        modifier = Modifier
                          .fillMaxWidth()
                          .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(days) { date ->
                            DateRowItem(
                                selectedDate, date,
                                onDateSelect = {
                                    viewModel.selectDate(date.toString())
                                })
                        }
                    }

                    if (list.value.list.isNullOrEmpty()) {
                        EmptyReminderState()
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(list.value.list) { item ->
                                ReminderItem(item = item, viewModel = viewModel, context)
                            }
                        }
                    }
                }
            }
        )
    }
}



