package com.example.reminderapp.domain.useCases

import com.example.reminderapp.data.source.ReminderRepository
import javax.inject.Inject

class GetRemindersUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {
     operator fun invoke() = reminderRepository.getAllReminders()

}