package com.pratistha.reminderapp.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun DateRowItem(selectedDate: MutableState<LocalDate>, date: LocalDate, onDateSelect:()->Unit) {
    val isSelected = selectedDate.value == date
    Box(
        modifier = Modifier
            .height(80.dp)
            .clip(RoundedCornerShape(50))
            .background(if (isSelected) MaterialTheme.colorScheme.tertiaryContainer else Color.LightGray)
            .clickable {
                selectedDate.value = date
                onDateSelect()
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = date.dayOfMonth.toString(),
                color = if (isSelected) Color.White else Color.Black,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = date.dayOfWeek.name.take(3),
                color = if (isSelected) Color.White else Color.DarkGray,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}