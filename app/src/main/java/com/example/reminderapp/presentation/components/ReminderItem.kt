package com.example.reminderapp.presentation.components

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.reminderapp.data.local.Reminder
import com.example.reminderapp.presentation.ReminderViewModel
import com.example.reminderapp.presentation.cancelAlarm

@Composable
fun ReminderItem(item: Reminder, viewModel: ReminderViewModel, context: Context) {
  Card(
    modifier = Modifier
      .padding(8.dp)
      .fillMaxWidth(),
    backgroundColor = if (item.isTaken) Color.Green else Color.White
  ) {
    Row(
      modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(text = item.name, style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = item.dosage, style = MaterialTheme.typography.body1)
      }

      Spacer(modifier = Modifier.weight(1f))

      IconButton(
        onClick = {
          cancelAlarm(context, item)
          viewModel.delete(item)
        }
      ) {
        Icon(Icons.Default.Delete, contentDescription = "Delete")
      }
    }
  }
}