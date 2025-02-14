package com.example.reminderapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TimeIntervalPickerDialog(
  showDialog: MutableState<Boolean>,
  onIntervalSelected: (Long) -> Unit
) {
  val selectedUnit = remember { mutableStateOf("Minutes") }
  val selectedValue = remember { mutableStateOf(0) }
  if (showDialog.value) {
    AlertDialog(
      onDismissRequest = { showDialog.value = false },
      title = { Text("Select Interval") },
      text = {

        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
          ) {
            Button(onClick = { selectedUnit.value = "Minutes" },
              colors = ButtonDefaults.buttonColors(
              containerColor = Color.White,
              contentColor = Color.Black),
              border = BorderStroke(1.dp, Color.Black),) {
              Text("Minutes")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { selectedUnit.value = "Hours" },
              colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black),
              border = BorderStroke(1.dp, Color.Black)) {
              Text("Hours")
            }
          }
          Spacer(modifier = Modifier.height(16.dp))

          Slider(
            value = selectedValue.value.toFloat(),
            onValueChange = { selectedValue.value = it.toInt() },
            valueRange = if (selectedUnit.value == "Minutes") 0f..60f else 1f..24f,
            steps = if (selectedUnit.value == "Minutes") 60 else 23,
            colors = SliderDefaults.colors(
              thumbColor = Color(0xFF006400),
              activeTrackColor = Color(0xFF006400),
              inactiveTrackColor = Color(0xFFC5E7C6),
            )
          )
          Text(
            text = "${selectedValue.value} ${selectedUnit.value}",
            modifier = Modifier.align(Alignment.CenterHorizontally)
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            showDialog.value = false
            val intervalInMillis = if (selectedUnit.value == "Minutes") {
              selectedValue.value * 60L * 1000L
            } else {
              selectedValue.value * 60L * 60L * 1000L
            }
            onIntervalSelected(intervalInMillis)
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF006400)
          )
        ) {
          Text("OK")
        }
      },
      containerColor = Color.White
    )
  }
}
