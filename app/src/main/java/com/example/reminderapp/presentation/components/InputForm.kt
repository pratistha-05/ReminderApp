package com.example.reminderapp.presentation.components

import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
@Composable
fun InputForm(
    time: String,
    onTimeClick: (String) -> Unit,
    onClick: (String, String, Boolean, Long) -> Unit
) {

    val name = remember { mutableStateOf("") }
    val dosage = remember { mutableStateOf("") }
    val context = LocalContext.current
    val isRepeat = remember { mutableStateOf(false) }
    val showIntervalDialog = remember { mutableStateOf(false) }
    val intervalTime = remember { mutableStateOf(0L) }

    Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Enter details",
            style= TextStyle(fontSize = 20.sp),
            modifier = Modifier.padding(top = 20.dp)
        )

        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = dosage.value,
            onValueChange = { dosage.value = it },
            label = { Text("Dosage") },
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = "Select Time")

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
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                  )
                  .clickable { showTimePicker(context, onTimeClick)  },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (time.contains(":")) time.split(":")[0] else "HH",
                    style = TextStyle(fontSize = 18.sp)
                )
            }
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = " : ",
                style = TextStyle(fontSize = 18.sp)
            )
            Box(
                modifier = Modifier
                  .weight(1f)
                  .height(50.dp)
                  .padding(4.dp)
                  .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                  )
                  .clickable {
                      showTimePicker(context,onTimeClick)
                  },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (time.contains(":")) time.split(":")[1] else "MM",
                    style = TextStyle(fontSize = 18.sp)
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
            Button(
                onClick = { showIntervalDialog.value = true }, enabled = isRepeat.value,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier.padding(2.dp)
            ) {
                Text(text = "Set Interval: ${if (intervalTime.value > 0) intervalTime.value / 1000 / 60 else "Not Set"} mins")
            }
        }
        Button(
            modifier = Modifier.padding(2.dp),
            onClick = {
                onClick(name.value, dosage.value, isRepeat.value, intervalTime.value)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF006400),
            )
        ) {
            Text(text = "Save", color = Color.White)
        }

        if (showIntervalDialog.value) {
            TimeIntervalPickerDialog(showIntervalDialog) { selectedInterval ->
                intervalTime.value = selectedInterval
            }
        }
    }
}
fun showTimePicker(context: Context, onTimeClick: (String) -> Unit) {
    val now = Calendar.getInstance()
    TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            val selected = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            if (selected.timeInMillis < now.timeInMillis) {
                Toast.makeText(context, "Cannot select a past time!", Toast.LENGTH_SHORT).show()
            } else {
                val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
                onTimeClick(formattedTime)
            }
        },
        now.get(Calendar.HOUR_OF_DAY),
        now.get(Calendar.MINUTE),
        true
    ).show()
}
@Preview
@Composable
fun InputFormPreview(){
  InputForm(
      time = "5:00",
      onTimeClick = { },
      onClick = { _, _, _, _ ->

      }
  )
}
