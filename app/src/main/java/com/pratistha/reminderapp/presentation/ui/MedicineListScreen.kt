package com.pratistha.reminderapp.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.pratistha.reminderapp.presentation.navigation.Screen
import com.pratistha.reminderapp.presentation.ui.components.MedicineCard
import com.pratistha.reminderapp.presentation.viewmodel.MedicineViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineListScreen(
    navController: NavController,
    viewModel: MedicineViewModel
) {
    val medicineList by viewModel.medicines.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchMedicines()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Medicines", color = MaterialTheme.colorScheme.secondary
                    )
                },
                actions = {

                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(medicineList, key = { it.id }) { medicine ->
                        MedicineCard(
                            medicine = medicine,
                            onEdit = {
                                viewModel.startEditing(it)
                                navController.navigate(Screen.AddMedicine.route)
                            }
                        )
                    }
                }
            }
        }
    )
}
