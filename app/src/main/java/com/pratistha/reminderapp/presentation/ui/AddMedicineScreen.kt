package com.pratistha.reminderapp.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.pratistha.reminderapp.data.local.Medicine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicineScreen() {

    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var purpose by remember { mutableStateOf("") }

    var lowStockReminder by remember {
        mutableStateOf(true)
    }

    var threshold by remember {
        mutableStateOf("5")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Add Medicine")
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Medicine Details",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = {
                    Text("Medicine Name")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = quantity,
                onValueChange = {
                    quantity = it.filter(Char::isDigit)
                },
                label = {
                    Text("Quantity In Stock")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = purpose,
                onValueChange = {
                    purpose = it
                },
                label = {
                    Text("Purpose")
                },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Low Stock Reminder",
                    modifier = Modifier.weight(1f)
                )

                Switch(
                    checked = lowStockReminder,
                    onCheckedChange = {
                        lowStockReminder = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                )
            }

            if (lowStockReminder) {

                OutlinedTextField(
                    value = threshold,
                    onValueChange = {
                        threshold = it.filter(Char::isDigit)
                    },
                    label = {
                        Text("Low Stock Threshold")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    if (
                        name.isBlank() ||
                        quantity.isBlank()
                    ) return@Button

                    val medicine = Medicine(
                        name = name,
                        quantity = quantity.toInt(),
                        purpose = purpose,
                        lowStockReminder = lowStockReminder,
                    )

                    FirebaseFirestore.getInstance()
                        .collection("medicines")
                        .add(medicine)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Text("Save Medicine")
            }
        }
    }
}