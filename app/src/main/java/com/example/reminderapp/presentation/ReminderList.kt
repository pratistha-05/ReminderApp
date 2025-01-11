package com.example.reminderapp.presentation

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.reminderapp.data.local.Reminder
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ReminderListUi(viewModel: ReminderViewModel = hiltViewModel()) {
  val uiState = viewModel.uiState.collectAsState()
  val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
  val scope = rememberCoroutineScope()
  val context = LocalContext.current

  val selectedTime = remember { mutableStateOf("") }

  ModalBottomSheetLayout(
    sheetState = sheetState,
    sheetContent = {
      InputForm(
        time = selectedTime.value,
        onTimeClick = { pickedTime ->
          selectedTime.value = pickedTime
        }
      ) { name, dosage ->
        val reminder = Reminder(
          name = name,
          dosage = dosage,
          timeinMillis = System.currentTimeMillis(),
          isTaken = false,
          isRepeat = false
        )
        viewModel.insert(reminder)
        alarmSetup(context, reminder)
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

@Composable
fun InputForm(
  time: String,
  onTimeClick: (String) -> Unit,
  onClick: (String, String) -> Unit
) {
  val name = remember { mutableStateOf("") }
  val dosage = remember { mutableStateOf("") }
  val context = LocalContext.current

  val timePickerDialog = remember {
    TimePickerDialog(
      context,
      { _, hourOfDay, minute ->
        val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
        onTimeClick(formattedTime)
      },
      12,
      0,
      true
    )
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Text(
      text = "Enter details",
      modifier = Modifier.padding(top = 20.dp)
    )

    OutlinedTextField(
      value = name.value,
      onValueChange = { name.value = it },
      label = { Text("Name") },
      modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
      value = dosage.value,
      onValueChange = { dosage.value = it },
      label = { Text("Dosage") },
      modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
      horizontalArrangement = Arrangement.Start,
    ) {
      Box(
        modifier = Modifier
          .weight(1f)
          .height(50.dp)
          .padding(4.dp)
          .border(
            width = 1.dp,
            color = androidx.compose.ui.graphics.Color.Gray,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
          )
          .clickable { timePickerDialog.show() },
        contentAlignment = androidx.compose.ui.Alignment.Center
      ) {
        Text(
          text = if (time.contains(":")) time.split(":")[0] else "HH",
          style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp)
        )
      }
      Text(
        modifier = Modifier.padding(top = 10.dp),
        text = " : ",
        style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp)
      )
      Box(
        modifier = Modifier
          .weight(1f)
          .height(50.dp)
          .padding(4.dp)
          .border(
              width = 1.dp,
              color = androidx.compose.ui.graphics.Color.Gray,
              shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            )
          .clickable { timePickerDialog.show()
        },
        contentAlignment = androidx.compose.ui.Alignment.Center
      ) {
        Text(
          text = if (time.contains(":")) time.split(":")[1] else "MM",
          style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp)
        )
      }
    }

    Button(
      onClick = { timePickerDialog.show() },
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(text = "Select Time")
    }
    Spacer(modifier = Modifier.height(16.dp))

    Button(onClick = {
      timePickerDialog.dismiss()
      onClick(name.value, dosage.value)
    }) {
      Text(text = "Save")
    }
  }
}

@Composable
fun ReminderItem(item: Reminder, viewModel: ReminderViewModel, context: Context) {
  Card(modifier = Modifier.padding(8.dp)) {
    Row(Modifier.padding(8.dp)) {
      Column {
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = item.name)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = item.dosage)
      }
      IconButton(onClick = {
        cancelAlarm(context, item)
        viewModel.delete(item)
      }) {
        Icon(Icons.Default.Delete, contentDescription = "Delete")
      }
    }
  }
}
