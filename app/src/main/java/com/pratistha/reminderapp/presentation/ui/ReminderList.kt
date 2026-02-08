package com.pratistha.reminderapp.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pratistha.reminderapp.data.local.Reminder
import com.pratistha.reminderapp.presentation.ui.components.DateRowItem
import com.pratistha.reminderapp.presentation.ui.components.EmptyReminderState
import com.pratistha.reminderapp.presentation.ui.components.InputForm
import com.pratistha.reminderapp.presentation.ui.components.ReminderItem
import com.pratistha.reminderapp.presentation.viewmodel.ReminderViewModel
import com.pratistha.reminderapp.utils.alarmSetup
import com.pratistha.reminderapp.utils.convertDateTimeToMillis
import kotlinx.coroutines.launch
import java.time.LocalDate
import com.pratistha.reminderapp.data.local.Frequency
import com.pratistha.reminderapp.utils.cancelAlarm
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ReminderListUi(viewModel: ReminderViewModel = hiltViewModel()) {
    val list = viewModel.list.collectAsState()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val context = LocalContext.current
    val selectedDateStr by viewModel.selectedDate.collectAsState()
    val selectedDate = remember(selectedDateStr) { LocalDate.parse(selectedDateStr) }
    val today = LocalDate.now()
    val editingReminder by viewModel.editingReminder.collectAsState()
    val days = remember {
        (0..6).map { today.plusDays(it.toLong()) }
    }
    val scope = rememberCoroutineScope()

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
                val slot = viewModel.slot.value

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
                        ToolTipIcon(
                            onAddClick = {
                                scope.launch {
                                    viewModel.clearEditing()
                                    sheetState.show()
                                }
                            }
                        )
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
                                        viewModel.selectDate(it.toString())
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

        /*potential ui issues:
        1. the edit works on previous time, it should not

        3. Timezone problem

        4. bottomsheet is hiding behind system ui

        4. if im adding reminder at 00:02 for next day, reminder is not
         */

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolTipIcon(onAddClick:()->Unit){
    val tooltipState = rememberTooltipState()

    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            RichTooltip(
                colors = TooltipDefaults.richTooltipColors(
                    containerColor = Color(0xFFE9F1EA),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        color = Color.Black,
                        text = "Tap here to add a new reminder",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        state = tooltipState
    ) {

        LaunchedEffect(Unit) {
            tooltipState.show()
            delay(8000)
            tooltipState.dismiss()
        }

        IconButton(
            onClick = { onAddClick()}
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Reminder")
        }
    }
}