package com.example.reminderapp.domain.useCases

import com.example.reminderapp.data.local.Reminder
import com.example.reminderapp.data.source.ReminderRepository
import javax.inject.Inject

class DeleteUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {
    suspend operator fun invoke(reminder: Reminder) = reminderRepository.delete(reminder)
}