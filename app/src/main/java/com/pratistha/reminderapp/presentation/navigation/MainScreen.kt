package com.pratistha.reminderapp.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pratistha.reminderapp.presentation.ui.AddMedicineScreen
import com.pratistha.reminderapp.presentation.ui.AddReminderScreen
import com.pratistha.reminderapp.presentation.ui.ReminderListUi
import com.pratistha.reminderapp.presentation.ui.components.BottomNavBar
import com.pratistha.reminderapp.presentation.ui.MedicineListScreen
import com.pratistha.reminderapp.presentation.viewmodel.MedicineViewModel
import com.pratistha.reminderapp.presentation.viewmodel.ReminderViewModel

@Composable
fun MainScreen(
    viewModel: ReminderViewModel,
    medicineViewModel: MedicineViewModel
) {

    var selectedTab by rememberSaveable {
        mutableIntStateOf(0)
    }
    val navController = rememberNavController()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            NavHost(
                navController = navController,
                startDestination = Screen.Home.route
            ) {

                composable(Screen.Home.route) {
                    ReminderListUi(
                        navController = navController,
                        viewModel = viewModel
                    )
                }

                composable(Screen.AddReminder.route) {
                    AddReminderScreen(
                        navController = navController,
                        viewModel = viewModel
                    )
                }

                composable(Screen.MedicineList.route) {
                    MedicineListScreen(navController,medicineViewModel)
                }

                composable(Screen.AddMedicine.route) {
                    AddMedicineScreen(
                        navController = navController,
                        viewModel = medicineViewModel
                    )
                }
            }
        }
        BottomNavBar(
            selectedIndex = selectedTab,
            onTabSelected = {

                selectedTab = it

                when(it) {

                    0 -> navController.navigate(Screen.Home.route)

                    1 -> navController.navigate(Screen.MedicineList.route)
                }
            },
            onAddClick = {

                when(selectedTab) {

                    0 -> {
                        viewModel.clearEditing()
                        navController.navigate(Screen.AddReminder.route)
                    }

                    1 -> {
                        medicineViewModel.clearEditing()
                        navController.navigate(Screen.AddMedicine.route)
                    }
                }
            }
        )
    }
}
