package com.pratistha.reminderapp.presentation.ui.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pratistha.reminderapp.R
import com.pratistha.reminderapp.data.local.Reminder
import com.pratistha.reminderapp.presentation.viewmodel.ReminderViewModel
import com.pratistha.reminderapp.utils.cancelAlarm
import com.pratistha.reminderapp.utils.convertMillisToTime

@Composable
fun ReminderItem(
    item: Reminder,
    viewModel: ReminderViewModel,
    context: Context,
    onEdit: (Reminder) -> Unit
) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = if (item.isTaken) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surface,
            contentColor = if (item.isTaken) Color(0xFF1B5E20) else MaterialTheme.colorScheme.onSurface
        ),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon / Image
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (item.isTaken) Color(0xFFC8E6C9) else MaterialTheme.colorScheme.primaryContainer,
                        androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.meds_icon),
                    contentDescription = "Medicine",
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text Information
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (item.isTaken) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                )
                Spacer(modifier = Modifier.height(6.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Time Chip
                    androidx.compose.material3.Surface(
                        color = if (item.isTaken) Color(0xFFA5D6A7) else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Alarm,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = convertMillisToTime(item.timeinMillis),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }

                    // Dosage Chip
                    androidx.compose.material3.Surface(
                        color = if (item.isTaken) Color(0xFFA5D6A7) else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "${item.dosage} mg",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                
                if (item.isRepeat) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Repeats ${item.frequency}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Actions
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!item.isTaken) {
                    IconButton(
                        onClick = { onEdit(item) }
                    ) {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    }
                }

                IconButton(
                    onClick = {
                        cancelAlarm(context, item)
                        viewModel.delete(item)
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}