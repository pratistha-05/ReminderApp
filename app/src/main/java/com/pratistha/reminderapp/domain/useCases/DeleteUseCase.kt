package com.pratistha.reminderapp.domain.useCases

import com.pratistha.reminderapp.data.local.Reminder
import com.pratistha.reminderapp.domain.repository.ReminderRepository
import javax.inject.Inject

class DeleteUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {
    suspend operator fun invoke(reminder: Reminder) = reminderRepository.delete(reminder)
}