package com.example.reminderapp.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.reminderapp.data.local.Reminder
import com.example.reminderapp.presentation.components.InputForm
import com.example.reminderapp.presentation.components.ReminderItem
import com.example.reminderapp.utils.convertTimeToMillis
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ReminderListUi(viewModel: ReminderViewModel = hiltViewModel()) {
  val uiState = viewModel.uiState.collectAsState()
  val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
  val scope = rememberCoroutineScope()
  val context = LocalContext.current

  val selectedTime = remember { mutableStateOf("") }

  val screenHeight = LocalConfiguration.current.screenHeightDp.dp

  ModalBottomSheetLayout(
    sheetState = sheetState,
    modifier = Modifier
      .fillMaxWidth() ,
    sheetContent = {
      InputForm(
        time = selectedTime.value,
        onTimeClick = { pickedTime ->
          selectedTime.value = pickedTime
        }
      ) { name, dosage,isRepeat, intervalTime ->
        val reminder = Reminder(
          name = name,
          dosage = dosage,
          timeinMillis = convertTimeToMillis(selectedTime.value),
          isTaken = false,
          isRepeat = isRepeat
        )
        viewModel.insert(reminder)
        if(isRepeat && intervalTime>0)
          setUpPeriodicAlarm(context,reminder, intervalTime)
        else
          alarmSetup(context, reminder)
        scope.launch {
          sheetState.hide()
        }
      }
    }
  ) {
    Scaffold(
      topBar = {
        CenterAlignedTopAppBar(
          title = { Text("Reminders") },
          actions = {
            IconButton(
              onClick = {
                scope.launch {
                  sheetState.show()
                }
              }
            ) {
              Icon(Icons.Default.Add, contentDescription = "Add Reminder")
            }
          }
        )
      },
      content = { paddingValues ->
        LazyColumn(
          modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
        ) {
          items(uiState.value.list) { item ->
            ReminderItem(item = item, viewModel = viewModel,context)
          }
        }
      }
    )
  }
}

