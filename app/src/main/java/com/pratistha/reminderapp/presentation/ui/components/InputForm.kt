package com.pratistha.reminderapp.presentation.ui.components

import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pratistha.reminderapp.utils.convertDateTimeToMillis
import java.time.LocalDate
import java.util.Calendar
@Composable
fun InputForm(
    selectedDate: LocalDate,
    time: String,
    onTimeClick: (String) -> Unit,
    onClick: (String, String, Boolean, Long) -> Unit
) {

    val name = remember { mutableStateOf("") }
    val dosage = remember { mutableStateOf(0) }
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
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF004D40),
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color(0xFF004D40),
                cursorColor = Color(0xFF004D40)
            )
        )
        Text(text = "Dosage")

        DosageCounterRow(
            dosage = dosage.value,
            onDosageChange = { newValue ->
                dosage.value = newValue
            }
        )

        Text(text = "Select Time")

        Row(
            horizontalArrangement = Arrangement.Start,
        ) {
            Box(
                modifier = Modifier
                  .weight(1f)
                  .height(50.dp)
                  .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                  )
                  .clickable { showTimePicker(context, onTimeClick,selectedDate)  },
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
                      showTimePicker(context,onTimeClick, selectedDate)
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
              .fillMaxWidth(),
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
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onClick(name.value, dosage.value.toString(), isRepeat.value, intervalTime.value)
                name.value = ""
                dosage.value = 0
                isRepeat.value = false
                intervalTime.value = 0L
            },
            colors = ButtonDefaults.buttonColors(
                containerColor =  MaterialTheme.colorScheme.tertiaryContainer)

        ) {
            Text(text = "Add Reminder", color = Color.White)
        }

        if (showIntervalDialog.value) {
            TimeIntervalPickerDialog(showIntervalDialog) { selectedInterval ->
                intervalTime.value = selectedInterval
            }
        }
    }
}
fun showTimePicker(
    context: Context,
    onTimeClick: (String) -> Unit,
    selectedDate: LocalDate,
    ) {
    val nowMillis = System.currentTimeMillis()
    val now = Calendar.getInstance()

    TimePickerDialog(
        context,
        { _, hourOfDay, minute ->

            val formattedTime = String.format("%02d:%02d", hourOfDay, minute)

            val selectedMillis = convertDateTimeToMillis(
                selectedDate,
                formattedTime
            )

            if (selectedMillis < nowMillis) {
                Toast.makeText(
                    context,
                    "Cannot select a past time!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.d(
                    "TimePicker",
                    "Selected date-time millis: $selectedMillis"
                )
                onTimeClick(formattedTime)
            }
        },
        now.get(Calendar.HOUR_OF_DAY),
        now.get(Calendar.MINUTE),
        true
    ).show()
}

@Composable
fun DosageCounterRow(
    dosage: Int,
    onDosageChange: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .height(50.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = { if (dosage > 0) onDosageChange(dosage - 1) }
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease",
                )
            }

            VerticalDivider()

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = dosage.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            VerticalDivider()

            IconButton(
                onClick = { onDosageChange(dosage + 1) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase",
                )
            }
        }
    }
}
