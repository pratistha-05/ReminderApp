package com.pratistha.reminderapp.presentation.navigation

sealed class Screen(val route: String) {
    object ReminderGraph : Screen("reminder_graph")
    object Home : Screen("home_screen")
    object AddReminder : Screen("add_reminder_screen")

    object MedicineGraph : Screen("medicine_graph")
    object MedicineList : Screen("medicine_list")
    object AddMedicine : Screen("add_medicine")
}
