package com.pratistha.reminderapp.domain.repository

import com.pratistha.reminderapp.data.local.Reminder
import kotlinx.coroutines.flow.Flow

interface  ReminderRepository {
    suspend fun insert(reminder: Reminder)
    suspend fun delete(reminder: Reminder)
    suspend fun update(reminder: Reminder)
    fun getAllReminders(): Flow<List<Reminder>>
    fun getRemindersForDate(date: String): Flow<List<Reminder>>
}