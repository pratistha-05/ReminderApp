package com.pratistha.reminderapp.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.pratistha.reminderapp.presentation.ui.AddMedicineScreen
import com.pratistha.reminderapp.presentation.ui.AddReminderScreen
import com.pratistha.reminderapp.presentation.ui.ReminderListUi
import com.pratistha.reminderapp.presentation.ui.components.BottomNavBar
import com.pratistha.reminderapp.presentation.ui.MedicineListScreen
import com.pratistha.reminderapp.presentation.viewmodel.MedicineViewModel
import com.pratistha.reminderapp.presentation.viewmodel.ReminderViewModel

@Composable
fun MainScreen() {

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
                startDestination = Screen.ReminderGraph.route
            ) {

                navigation(
                    startDestination = Screen.Home.route,
                    route = Screen.ReminderGraph.route
                ) {
                    composable(Screen.Home.route) { backStackEntry ->
                        val parentEntry = remember(backStackEntry) {
                            navController.getBackStackEntry(Screen.ReminderGraph.route)
                        }
                        val viewModel: ReminderViewModel = hiltViewModel(parentEntry)
                        ReminderListUi(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }

                    composable(Screen.AddReminder.route) { backStackEntry ->
                        val parentEntry = remember(backStackEntry) {
                            navController.getBackStackEntry(Screen.ReminderGraph.route)
                        }
                        val viewModel: ReminderViewModel = hiltViewModel(parentEntry)
                        AddReminderScreen(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                }

                navigation(
                    startDestination = Screen.MedicineList.route,
                    route = Screen.MedicineGraph.route
                ) {
                    composable(Screen.MedicineList.route) { backStackEntry ->
                        val parentEntry = remember(backStackEntry) {
                            navController.getBackStackEntry(Screen.MedicineGraph.route)
                        }
                        val medicineViewModel: MedicineViewModel = hiltViewModel(parentEntry)
                        MedicineListScreen(navController, medicineViewModel)
                    }

                    composable(Screen.AddMedicine.route) { backStackEntry ->
                        val parentEntry = remember(backStackEntry) {
                            navController.getBackStackEntry(Screen.MedicineGraph.route)
                        }
                        val medicineViewModel: MedicineViewModel = hiltViewModel(parentEntry)
                        AddMedicineScreen(
                            navController = navController,
                            viewModel = medicineViewModel
                        )
                    }
                }
            }
        }
        BottomNavBar(
            selectedIndex = selectedTab,
            onTabSelected = {
                selectedTab = it
                when(it) {
                    0 -> navController.navigate(Screen.ReminderGraph.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    1 -> navController.navigate(Screen.MedicineGraph.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            onAddClick = {
                when(selectedTab) {
                    0 -> navController.navigate(Screen.AddReminder.route)
                    1 -> navController.navigate(Screen.AddMedicine.route)
                }
            }
        )
    }
}
