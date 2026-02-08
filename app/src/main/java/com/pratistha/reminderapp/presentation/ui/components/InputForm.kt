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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pratistha.reminderapp.utils.convertDateTimeToMillis
import com.pratistha.reminderapp.presentation.viewmodel.ReminderViewModel
import com.pratistha.reminderapp.data.local.Frequency
import java.time.LocalDate
import java.util.Calendar

@Composable
fun InputForm(
    viewModel: ReminderViewModel,
    onTimeClick: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    val name by viewModel.reminderName.collectAsState()
    val dosage by viewModel.reminderDosage.collectAsState()
    val time by viewModel.reminderTime.collectAsState()
    val isRepeat by viewModel.isRepeat.collectAsState()
    val frequency by viewModel.frequency.collectAsState()
    val isEditable by viewModel.editingReminder.collectAsState()
    val focusManager = LocalFocusManager.current

    val context = LocalContext.current
    val selectedDateStr by viewModel.selectedDate.collectAsState()
    val selectedDate = remember(selectedDateStr) { LocalDate.parse(selectedDateStr) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
//        Text(
//            text = "Add a new reminder",
//            style = TextStyle(fontSize = 20.sp, color = Color(0xFF004D40)),
//            modifier = Modifier.padding(top = 20.dp).fillMaxWidth(),
//            textAlign = TextAlign.Center
//        )
//        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Enter details",
            style = TextStyle(fontSize = 20.sp),
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            maxLines = 1,
            value = name,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            onValueChange = { viewModel.onNameChange(it) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF004D40),
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color(0xFF004D40),
                cursorColor = Color(0xFF004D40)
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Dosage")
        Spacer(modifier = Modifier.height(4.dp))

        DosageCounterRow(
            dosage = dosage,
            onDosageChange = { newValue ->
                viewModel.onDosageChange(newValue)
            }
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Select Time")
        Spacer(modifier = Modifier.height(4.dp))

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
                    .clickable { showTimePicker(context, onTimeClick, selectedDate) },
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
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        showTimePicker(context, onTimeClick, selectedDate)
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
            Text(text = "Select Slots")
        }
        Spacer(modifier = Modifier.height(8.dp))

        val slots = listOf("Before Meal", "After Meal", "Morning", "Evening", "Before Sleep")
        val selectedSlot by viewModel.slot.collectAsState()

        androidx.compose.foundation.lazy.LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(slots) { slotOption ->
                val isSelected = slotOption == selectedSlot
                Box(
                    modifier = Modifier
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { viewModel.onSlotChange(if (isSelected) "" else slotOption) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = slotOption,
                        color = if (isSelected) Color.White else Color.Black,
                        style = TextStyle(fontSize = 14.sp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Repeat Alarm?")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isRepeat,
                onCheckedChange = { viewModel.onRepeatChange(it) }
            )
        }

        if (isRepeat) {
            FrequencyDropdown(
                selected = frequency,
                onSelected = { viewModel.onFrequencyChange(it) }
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onSaveClick()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )

        ) {
            if (isEditable == null)
                Text(text = "Add Reminder", color = Color.White)
            else
                Text(text = "Save", color = Color.White)
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
