package com.example.reminderapp.domain.repository

import com.example.reminderapp.data.local.Reminder
import kotlinx.coroutines.flow.Flow

interface  ReminderRepository {
    suspend fun insert(reminder: Reminder)
    suspend fun delete(reminder: Reminder)
    suspend fun update(reminder: Reminder)
    fun getAllReminders(): Flow<List<Reminder>>

}