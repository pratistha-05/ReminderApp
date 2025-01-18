package com.example.reminderapp.presentation.components

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.AlarmOff
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.reminderapp.data.local.Reminder
import com.example.reminderapp.presentation.ReminderViewModel
import com.example.reminderapp.presentation.cancelAlarm
import com.example.reminderapp.utils.convertMillisToTime
@Composable
fun ReminderItem(item: Reminder, viewModel: ReminderViewModel, context: Context) {
  Card(
    modifier = Modifier
      .padding(8.dp)
      .fillMaxWidth()
      .shadow(4.dp, shape = RoundedCornerShape(8.dp)),
    shape = RoundedCornerShape(8.dp),
    elevation = 4.dp,
    backgroundColor = if (item.isTaken) Color.Green.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.15f)
  ) {
    Row(
      modifier = Modifier
        .padding(12.dp)
        .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(
        modifier = Modifier.weight(1f).padding(start = 8.dp)
      ) {
        Text(
          text = item.name,
          style = MaterialTheme.typography.h6,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Opacity,
            contentDescription = "Dosage",
            tint = Color.Gray,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = item.dosage,
            style = MaterialTheme.typography.body1,
            color = Color.Gray
          )

          Spacer(modifier = Modifier.width(26.dp))

          if(item.isTaken) {
            Icon(
              imageVector = Icons.Outlined.Alarm,
              contentDescription = "Reminder Time",
              tint = Color.Gray,
              modifier = Modifier.size(14.dp)
            )
          }
          else{
            Icon(
              imageVector = Icons.Outlined.AlarmOff,
              contentDescription = "Reminder Time",
              tint = Color.Gray,
              modifier = Modifier.size(14.dp)
            )
          }
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = convertMillisToTime(item.timeinMillis),
            style = MaterialTheme.typography.body1,
            color = Color.Gray
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = if (item.isRepeat) "On Repeat" else "One-Time",
          style = MaterialTheme.typography.body1,
          fontWeight = FontWeight.SemiBold
        )
      }

      IconButton(
        onClick = {
          cancelAlarm(context, item)
          viewModel.delete(item)
        }
      ) {
        Icon(
          imageVector = Icons.Default.Delete,
          contentDescription = "Delete",
        )
      }
    }
  }
}
