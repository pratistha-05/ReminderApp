package com.example.reminderapp.ui.theme

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.reminderapp.presentation.ReminderViewModel
import com.example.reminderapp.presentation.cancelAlarm
import kotlinx.coroutines.launch
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ReminderListUi(viewModel: ReminderViewModel= hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val context= LocalContext.current


    ModalBottomSheetLayout(
        sheetState=sheetState,
        sheetContent = {
            InputForm(time="", onTimeClick = {}){ name,dosage->
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Reminders") },
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                sheetState.show()
                            }
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "Add Reminder")
                        }
                    }
                )
            },
            content = { it ->
                // Empty content for now
                LazyColumn(modifier = Modifier.padding(it).fillMaxSize()) {
                    items(uiState.value.list) { item ->
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
                }
            }
        )
    }
}


@Composable
fun InputForm(time: String, onTimeClick: () -> Unit, onClick: (String, String) -> Unit) {

    val name = remember { mutableStateOf("") }
    val dosage = remember { mutableStateOf("") }
    val date = remember { mutableStateOf("") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            date.value = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

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

        // Date Picker Field
        OutlinedTextField(
            value = date.value,
            onValueChange = { date.value = it },
            label = { Text("Select Date") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { datePickerDialog.show() },
            readOnly = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { onClick.invoke(name.value, dosage.value) }) {
            Text(text = "Save")
        }
        Spacer(modifier = Modifier.height(20.dp))
    }

}

