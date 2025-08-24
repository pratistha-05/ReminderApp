package com.example.reminderapp.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.reminderapp.data.local.Reminder
import com.example.reminderapp.presentation.components.InputForm
import com.example.reminderapp.presentation.components.ReminderItem
import com.example.reminderapp.utils.convertTimeToMillis
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ReminderListUi(viewModel: ReminderViewModel = hiltViewModel()) {
  val uiState = viewModel.uiState.collectAsState()
  val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
  val scope = rememberCoroutineScope()
  val context = LocalContext.current

  val selectedTime = remember { mutableStateOf("") }
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
        val reminder = Reminder(
          name = name,
          dosage = dosage,
          timeinMillis = convertTimeToMillis(selectedTime.value),
          isTaken = false,
          isRepeat = isRepeat
        )
        viewModel.insert(reminder)
        if (isRepeat && intervalTime > 0)
          setUpPeriodicAlarm(context, reminder, intervalTime)
        else
          alarmSetup(context, reminder)
        scope.launch { sheetState.hide() }
      }
    }
  ) {
    Scaffold(
      topBar = {
        CenterAlignedTopAppBar(
          title = { Text("Reminders") },
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
          LazyRow (
            modifier = Modifier
              .fillMaxWidth()
              .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(days) { date ->
              DateItem(selectedDate,date)
            }
          }

          LazyColumn(
            modifier = Modifier.fillMaxSize()
          ) {
            items(uiState.value.list) { item ->
              ReminderItem(item = item, viewModel = viewModel, context)
            }
          }
        }
      }
    )
  }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateItem(selectedDate: MutableState<LocalDate>, date: LocalDate) {
  val isSelected = selectedDate.value == date
  Box(
    modifier = Modifier
      .height(80.dp)
      .clip(RoundedCornerShape(50))
      .background(if (isSelected) Color(0xFF004D40) else Color.LightGray)
      .clickable { selectedDate.value = date }
      .padding(horizontal = 16.dp, vertical = 12.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = date.dayOfMonth.toString(),
        color = if (isSelected) Color.White else Color.Black,
        style = MaterialTheme.typography.bodyLarge
      )
      Text(
        text = date.dayOfWeek.name.take(3),
        color = if (isSelected) Color.White else Color.DarkGray,
        style = MaterialTheme.typography.bodySmall
      )
    }
  }
}

