package com.pratistha.reminderapp


import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pratistha.reminderapp.presentation.ui.AddReminderScreen
import com.pratistha.reminderapp.presentation.ui.ReminderListUi
import com.pratistha.reminderapp.ui.theme.ReminderAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(ma
                    this,
                    arrayOf(POST_NOTIFICATIONS),
                    1001
                )
            }
        }
        setContent {
            ReminderAppTheme(darkTheme = false) {
                val navController = rememberNavController()
                val sharedViewModel: com.pratistha.reminderapp.presentation.viewmodel.ReminderViewModel = androidx.hilt.navigation.compose.hiltViewModel()

                androidx.navigation.compose.NavHost(
                    navController = navController,
                    startDestination = com.pratistha.reminderapp.presentation.navigation.Screen.Home.route
                ) {
                    composable(com.pratistha.reminderapp.presentation.navigation.Screen.Home.route) {
                        ReminderListUi(navController = navController, viewModel = sharedViewModel)
                    }
                    composable(com.pratistha.reminderapp.presentation.navigation.Screen.AddReminder.route) {
                        AddReminderScreen(navController = navController, viewModel = sharedViewModel)
                    }
                }
            }
        }
    }
}
/*
P0:
test the datewise remiders
change the ui
1. settings new screen, top bar change
2. validations in input field
3. swipe to delete or taken
4. to take(upcoming), taken
5. ai chat bot for reminders
 */