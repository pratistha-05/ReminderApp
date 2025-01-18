package com.example.reminderapp.presentation.components

import android.app.TimePickerDialog
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Colors
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputForm(
  time: String,
  onTimeClick: (String) -> Unit,
  onClick: (String, String,Boolean,Long) -> Unit
) {
  val name = remember { mutableStateOf("") }
  val dosage = remember { mutableStateOf("") }
  val context = LocalContext.current
  val isRepeat = remember { mutableStateOf(false) }
  val showIntervalDialog = remember { mutableStateOf(false) }
  val intervalTime = remember { mutableStateOf(0L) }

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

    Spacer(modifier = Modifier.height(5.dp))
    OutlinedTextField(
      value = dosage.value,
      onValueChange = { dosage.value = it },
      label = { Text("Dosage") },
      modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(5.dp))

    Text(
      text = "Select Time")

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
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text = "Repeat Alarm?")
      Spacer(modifier = Modifier.width(8.dp))
      Switch(
        checked = isRepeat.value,
        onCheckedChange = { isRepeat.value = it }
      )
    }
    if (isRepeat.value) {
      Button(onClick = { showIntervalDialog.value = true }, enabled = isRepeat.value) {
        Text(text = "Set Interval: ${if (intervalTime.value > 0) intervalTime.value / 1000 / 60 else "Not Set"} mins")
      }
    }
    Button(onClick = {
      timePickerDialog.dismiss()
      onClick(name.value, dosage.value, isRepeat.value, intervalTime.value)
    }) {
      Text(text = "Save")
    }

    if (showIntervalDialog.value) {
      TimeIntervalPickerDialog(showIntervalDialog) { selectedInterval ->
        intervalTime.value = selectedInterval
      }
    }
  }
}
