package com.pratistha.reminderapp.presentation.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.SettingsVoice
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pratistha.reminderapp.presentation.ui.components.DateRowItem
import com.pratistha.reminderapp.presentation.ui.components.EmptyReminderState
import com.pratistha.reminderapp.presentation.ui.components.listItem.ReminderItem
import com.pratistha.reminderapp.presentation.viewmodel.ReminderViewModel
import java.time.LocalDate

import androidx.navigation.NavController
import com.pratistha.reminderapp.presentation.navigation.Screen
import com.pratistha.reminderapp.presentation.ui.components.listItem.SwipeBackground
import com.pratistha.reminderapp.utils.alarm.cancelAlarm
import com.pratistha.reminderapp.utils.voice.handleVoiceResult
import com.pratistha.reminderapp.utils.voice.startVoiceRecognition

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ReminderListUi(
    navController: NavController,
    viewModel: ReminderViewModel = hiltViewModel()
) {
    val list = viewModel.list.collectAsState()
    val today = LocalDate.now()
    val selectedDateStr by viewModel.selectedDate.collectAsState()
    val context = LocalContext.current
    val selectedDate = remember(selectedDateStr) { LocalDate.parse(selectedDateStr) }
    val days = remember {
        (0..6).map { today.plusDays(it.toLong()) }
    }
    val voiceLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            handleVoiceResult(
                result.resultCode,
                result.data,
                viewModel,
                context,
                call={

                }
            )
        }

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
                            startVoiceRecognition(voiceLauncher)
//                            viewModel.clearEditing()
//                            navController.navigate(Screen.AddReminder.route)
                        }
                    )
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
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
                                items(list.value.list, key = { it.id }) { item ->

                                    val dismissState = rememberDismissState(
                                        confirmStateChange = { dismissValue ->

                                            if (dismissValue == DismissValue.DismissedToStart) {
                                                cancelAlarm(context, item)
                                                viewModel.update(item.copy(isTaken = true))
                                            }
                                            false
                                        }
                                    )

                                    SwipeToDismiss(
                                        state = dismissState,
                                        directions = setOf(DismissDirection.EndToStart),
                                        background = {
                                            SwipeBackground(dismissState)
                                        },
                                        dismissContent = {
                                            ReminderItem(
                                                item = item,
                                                viewModel = viewModel,
                                                context = context
                                            ) { reminderToEdit ->
                                                viewModel.editReminder(reminderToEdit)
                                                viewModel.selectDate(reminderToEdit.date)
                                                navController.navigate(Screen.AddReminder.route)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
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

//        LaunchedEffect(Unit) {
//            tooltipState.show()
//            delay(8000)
//            tooltipState.dismiss()
//        }

        IconButton(
            onClick = { onAddClick()}
        ) {
            Icon(Icons.Default.SettingsVoice, contentDescription = "Add Reminder")
        }
    }
}