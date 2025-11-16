package com.example.reminderapp.domain.useCases

import com.example.reminderapp.domain.repository.ReminderRepository
import javax.inject.Inject

class GetRemindersUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {
     fun getAllReminders() = reminderRepository.getAllReminders()

     fun getRemindersForDate(date: String) = reminderRepository.getRemindersForDate(date)
}