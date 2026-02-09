package com.pratistha.reminderapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratistha.reminderapp.data.local.Frequency
import com.pratistha.reminderapp.data.local.Reminder
import com.pratistha.reminderapp.domain.useCases.DeleteUseCase
import com.pratistha.reminderapp.domain.useCases.GetRemindersUseCase
import com.pratistha.reminderapp.domain.useCases.InsertReminderUseCase
import com.pratistha.reminderapp.domain.useCases.UpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val insertUseCase: InsertReminderUseCase,
    private val getRemindersUseCase: GetRemindersUseCase,
    private val deleteUseCase: DeleteUseCase,
    private val updateUseCase: UpdateUseCase
): ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now().toString())
    val selectedDate = _selectedDate.asStateFlow()

    private val _editingReminder = MutableStateFlow<Reminder?>(null)
    val editingReminder = _editingReminder.asStateFlow()

    // Form State
    private val _reminderName = MutableStateFlow("")
    val reminderName = _reminderName.asStateFlow()

    private val _reminderDosage = MutableStateFlow(0)
    val reminderDosage = _reminderDosage.asStateFlow()

    private val _reminderTime = MutableStateFlow("")
    val reminderTime = _reminderTime.asStateFlow()

    private val _isRepeat = MutableStateFlow(false)
    val isRepeat = _isRepeat.asStateFlow()

    private val _frequency = MutableStateFlow(Frequency.Daily)
    val frequency = _frequency.asStateFlow()

    private val _slot = MutableStateFlow("")
    val slot = _slot.asStateFlow()

    fun onNameChange(newName: String) { _reminderName.value = newName }
    fun onDosageChange(newDosage: Int) { _reminderDosage.value = newDosage }
    fun onTimeChange(newTime: String) { _reminderTime.value = newTime }
    fun onRepeatChange(repeat: Boolean) { _isRepeat.value = repeat }
    fun onFrequencyChange(newFrequency: Frequency) { _frequency.value = newFrequency }
    fun onSlotChange(newSlot: String) { _slot.value = newSlot }

    fun editReminder(reminder: Reminder) {
        _editingReminder.value = reminder
        _reminderName.value = reminder.name
        _reminderDosage.value = reminder.dosage.toIntOrNull() ?: 0
        _reminderTime.value = com.pratistha.reminderapp.utils.convertMillisToTime(reminder.timeinMillis)
        _isRepeat.value = reminder.isRepeat
        _frequency.value = Frequency.values().find { it.value == reminder.frequency } ?: Frequency.Daily
        _slot.value = reminder.slot
    }

    fun clearEditing() {
        _editingReminder.value = null
        _reminderName.value = ""
        _reminderDosage.value = 0
        _reminderTime.value = ""
        _isRepeat.value = false
        _frequency.value = Frequency.Daily
        _slot.value = ""
    }


    val list: StateFlow<ReminderUiState> =
        selectedDate
            .flatMapLatest { date ->
                    getRemindersUseCase.getRemindersForDate(date)
            }
            .map { ReminderUiState(list = it, isLoading = false) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                ReminderUiState(isLoading = true)
            )

    fun selectDate(date: String) {
        _selectedDate.value = date
    }

    fun insert(reminder: Reminder) = viewModelScope.launch {
        insertUseCase.invoke(reminder)
    }

    suspend fun insertSuspend(reminder: Reminder): Long {
        return insertUseCase.invoke(reminder)
    }

    fun delete(reminder: Reminder) = viewModelScope.launch {
        deleteUseCase.invoke(reminder)
    }

    fun update(reminder: Reminder) = viewModelScope.launch {
        updateUseCase.invoke(reminder)

    }
}
data class ReminderUiState(
    val list: List<Reminder> = emptyList(),
    val isLoading: Boolean = true
)