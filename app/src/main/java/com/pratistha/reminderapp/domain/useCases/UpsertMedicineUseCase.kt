package com.pratistha.reminderapp.domain.useCases

import com.pratistha.reminderapp.data.local.Medicine
import com.pratistha.reminderapp.domain.repository.ReminderRepository
import javax.inject.Inject

class UpsertMedicineUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {
    suspend operator fun invoke(medicine: Medicine) = reminderRepository.upsertMedicine(medicine)
}
