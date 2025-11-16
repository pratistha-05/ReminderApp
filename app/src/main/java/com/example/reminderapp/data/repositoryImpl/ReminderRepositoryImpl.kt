package com.example.reminderapp.data.repositoryImpl

import com.example.reminderapp.data.local.Reminder
import com.example.reminderapp.data.local.dao.ReminderDao
import com.example.reminderapp.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow

class ReminderRepositoryImpl(private val reminderDao: ReminderDao): ReminderRepository {
    override suspend fun insert(reminder: Reminder) {
        reminderDao.insert(reminder)
    }

    override suspend fun delete(reminder: Reminder) {
        reminderDao.delete(reminder)
    }

    override suspend fun update(reminder: Reminder) {
        reminderDao.update(reminder)
    }

    override fun getAllReminders(): Flow<List<Reminder>> = reminderDao.getAllReminder()

    override fun getRemindersForDate(date: String): Flow<List<Reminder>> {
        return reminderDao.getRemindersByDate(date)
    }

}