package com.example.reminderapp.presentation.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.reminderapp.data.local.Reminder
import com.example.reminderapp.presentation.viewmodel.ReminderViewModel
import com.example.reminderapp.utils.cancelAlarm
import com.example.reminderapp.utils.convertMillisToTime

@Composable
fun ReminderItem(item: Reminder, viewModel: ReminderViewModel, context: Context) {

    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(25))
            .background(
                color = if (item.isTaken)
                    MaterialTheme.colorScheme.tertiary
                else
                    MaterialTheme.colorScheme.onSurface,
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp)
        ) {

            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Opacity,
                        contentDescription = "Dosage",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${item.dosage} mg",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (item.isTaken) Icons.Filled.Alarm else Icons.Outlined.Alarm,
                        contentDescription = "Time",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = convertMillisToTime(item.timeinMillis),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (item.isRepeat) "Repeats Daily" else "One-Time",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            )
        }


        IconButton(
            onClick = {
                cancelAlarm(context, item)
                viewModel.delete(item)
            },
            modifier = Modifier.align(Alignment.Top) // cleaner UI
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
    }
