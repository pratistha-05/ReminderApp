package com.pratistha.reminderapp.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import com.pratistha.reminderapp.presentation.viewmodel.ReminderViewModel

@Composable
fun MainScreen(
    viewModel: ReminderViewModel
) {

    var selectedTab by rememberSaveable {
        mutableIntStateOf(0)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier =  Modifier
                    .weight(1f)
                .fillMaxWidth()
        ) {

            when (selectedTab) {

                0 -> {

                    val navController = rememberNavController()

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
                    }
                }

                1 -> {
                    AddMedicineScreen()
                }
            }
        }
        BottomNavBar(
            selectedIndex = selectedTab,
            onTabSelected = {
                selectedTab = it
            }
        )
    }
}
