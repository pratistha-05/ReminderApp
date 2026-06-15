package com.pratistha.reminderapp.domain.useCases

import com.pratistha.reminderapp.data.local.Medicine
import com.pratistha.reminderapp.data.local.Reminder
import com.pratistha.reminderapp.domain.repository.ReminderRepository
import javax.inject.Inject

class GetMedicineUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {
    operator fun invoke() = reminderRepository.getMedicines()
}