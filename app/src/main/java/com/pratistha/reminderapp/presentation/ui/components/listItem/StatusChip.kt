package com.pratistha.reminderapp.presentation.ui.components.listItem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pratistha.reminderapp.data.local.Reminder

@Composable
fun StatusChip(item: Reminder) {

    val now = System.currentTimeMillis()

    val (text, color, icon) = when {
        item.isTaken -> {
            Triple("Taken", Color(0xFF0B500E), Icons.Default.Check)
        }
        item.timeinMillis > now -> {
            Triple("Upcoming", Color(0xFFFFA726), null)
        }
        else -> {
            Triple("Skipped", Color(0xFFD32F2F), Icons.Default.DoNotDisturb)
        }
    }

    Surface(
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, color),
        color = color.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }

            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
