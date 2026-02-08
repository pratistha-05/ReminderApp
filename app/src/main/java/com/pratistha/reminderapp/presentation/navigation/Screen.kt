package com.pratistha.reminderapp.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object AddReminder : Screen("add_reminder_screen")
}
