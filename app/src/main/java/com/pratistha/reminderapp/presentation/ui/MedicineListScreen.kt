package com.pratistha.reminderapp.presentation.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pratistha.reminderapp.presentation.navigation.Screen
import com.pratistha.reminderapp.presentation.ui.components.DateRowItem
import com.pratistha.reminderapp.presentation.ui.components.EmptyReminderState
import com.pratistha.reminderapp.presentation.ui.components.MedicineCard
import com.pratistha.reminderapp.presentation.ui.components.listItem.ReminderItem
import com.pratistha.reminderapp.presentation.ui.components.listItem.SwipeBackground
import com.pratistha.reminderapp.presentation.viewmodel.MedicineViewModel
import com.pratistha.reminderapp.presentation.viewmodel.ReminderViewModel
import com.pratistha.reminderapp.utils.alarm.cancelAlarm
import com.pratistha.reminderapp.utils.voice.handleVoiceResult
import com.pratistha.reminderapp.utils.voice.startVoiceRecognition
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineListScreen(
    navController: NavController,
    viewModel: MedicineViewModel
) {
    val context = LocalContext.current
    val medicineList by viewModel.medicines.collectAsStateWithLifecycle()

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